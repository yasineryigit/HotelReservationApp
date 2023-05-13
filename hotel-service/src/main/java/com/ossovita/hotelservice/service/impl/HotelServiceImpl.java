package com.ossovita.hotelservice.service.impl;

import com.ossovita.clients.user.UserClient;
import com.ossovita.commonservice.exception.IdNotFoundException;
import com.ossovita.commonservice.exception.UserNotFoundException;
import com.ossovita.commonservice.payload.request.HotelEmployeeRequest;
import com.ossovita.commonservice.payload.request.HotelRequest;
import com.ossovita.hotelservice.entity.*;
import com.ossovita.hotelservice.payload.request.HotelWithImagesRequest;
import com.ossovita.hotelservice.payload.response.HotelEmployeeResponse;
import com.ossovita.hotelservice.repository.*;
import com.ossovita.hotelservice.service.HotelService;
import com.ossovita.hotelservice.utils.file.FileService;
import com.ossovita.hotelservice.utils.file.ImageResponse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class HotelServiceImpl implements HotelService {

    @Value("${file.hotel-images.upload-dir}")
    private String hotelImagesUploadDir;
    @Value("${hotel-service.api-prefix}")
    String apiPrefix;
    HotelRepository hotelRepository;
    AddressRepository addressRepository;
    HotelEmployeeRepository hotelEmployeeRepository;
    HotelBossRepository hotelBossRepository;
    RoomRepository roomRepository;
    HotelImageRepository hotelImageRepository;
    UserClient userClient;
    ModelMapper modelMapper;
    FileService fileService;

    public HotelServiceImpl(HotelRepository hotelRepository, AddressRepository addressRepository, HotelEmployeeRepository hotelEmployeeRepository, HotelBossRepository hotelBossRepository, RoomRepository roomRepository, HotelImageRepository hotelImageRepository, UserClient userClient, ModelMapper modelMapper, FileService fileService) {
        this.hotelRepository = hotelRepository;
        this.addressRepository = addressRepository;
        this.hotelEmployeeRepository = hotelEmployeeRepository;
        this.hotelBossRepository = hotelBossRepository;
        this.roomRepository = roomRepository;
        this.hotelImageRepository = hotelImageRepository;
        this.userClient = userClient;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
    }

    @Override
    public Hotel createHotel(HotelRequest hotelRequest) {
        //we need to check employeeFk is real or not

        Address address = modelMapper.map(hotelRequest, Address.class);
        Address savedAddress = addressRepository.save(address);

        Hotel hotel = modelMapper.map(hotelRequest, Hotel.class);
        hotel.setAddressFk(savedAddress.getAddressPk());
        hotel.setAddress(address);//to give more info at post request return

        Hotel savedHotel = hotelRepository.save(hotel);

        //also save to the hotelemployees table
        createHotelBoss(savedHotel.getHotelPk(), hotelRequest.getBossFk());

        return savedHotel;
    }

    @Override
    public Hotel createHotelWithHotelImages(HotelWithImagesRequest hotelWithImagesRequest) throws IOException {
        //check if boss is available in boss table
        if ((userClient.isBossAvailable(hotelWithImagesRequest.getBossFk()))) {
            //save address
            Address address = modelMapper.map(hotelWithImagesRequest, Address.class);
            Address savedAddress = addressRepository.save(address);

            Hotel hotel = modelMapper.map(hotelWithImagesRequest, Hotel.class);
            hotel.setAddressFk(savedAddress.getAddressPk());
            hotel.setAddress(address);//to give more info at post request return

            Hotel savedHotel = hotelRepository.save(hotel);


            //save hotelImage

            for (MultipartFile file : hotelWithImagesRequest.getHotelImages()) {

                ImageResponse imageResponse = fileService.saveImage(file, hotelImagesUploadDir);
                HotelImage hotelImage = new HotelImage();
                hotelImage.setHotelImageName(imageResponse.getFileName());
                hotelImage.setHotelImageUrl(apiPrefix + imageResponse.getImageUrl());// make url: /api/1.0/hotel/hotel-service/uploads/hotel-images/dsfks231asd.jpg
                hotelImage.setHotelFk(savedHotel.getHotelPk());

                hotelImageRepository.save(hotelImage);
            }

            //also save to hotel_employees table at the hotel-service
            createHotelBoss(savedHotel.getHotelPk(), hotelWithImagesRequest.getBossFk());

            return savedHotel;

        } else throw new IdNotFoundException("Boss not found by the given id");

    }

    @Override
    public HotelEmployeeResponse createHotelEmployee(HotelEmployeeRequest hotelEmployeeRequest) {
        hotelRepository.findById(hotelEmployeeRequest.getHotelFk()).orElseThrow(() -> new IdNotFoundException("Hotel not found by the given id"));
        HotelEmployee hotelEmployee = modelMapper.map(hotelEmployeeRequest, HotelEmployee.class);
        HotelEmployee savedHotelEmployee = hotelEmployeeRepository.save(hotelEmployee);
        return modelMapper.map(savedHotelEmployee, HotelEmployeeResponse.class);
    }

    /*
     * it checks bossFk from user-service, then create HotelBoss object in hotel-service
     * */
    public void createHotelBoss(long hotelFk, long bossFk) {
        //we need to check bossFk is real or not
        if (userClient.isBossAvailable(bossFk)) {
            //also save to the hotelemployees table
            HotelBoss hotelBoss = HotelBoss.builder()
                    .hotelFk(hotelFk)
                    .bossFk(bossFk)
                    .build();
            hotelBossRepository.save(hotelBoss);
        } else {
            log.error("HotelManager | createHotel | Boss not found by the given id");
            throw new UserNotFoundException("Boss not found by the given id");
        }

    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }


    @Override
    public boolean isHotelAvailable(long hotelPk) {
        return hotelRepository.existsByHotelPk(hotelPk);
    }


}


