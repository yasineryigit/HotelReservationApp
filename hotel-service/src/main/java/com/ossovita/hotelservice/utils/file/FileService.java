package com.ossovita.hotelservice.utils.file;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileService {


    @Value("${spring.application.name}")
    String serviceName;

    public ImageResponse saveImage(MultipartFile file, String uploadDir) throws IOException {

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        if (!Objects.equals(file.getContentType(), "image/png") && !Objects.equals(file.getContentType(), "image/jpeg")) {
            throw new IOException("File should be .jpg or .png");
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = FilenameUtils.getExtension(fileName);
        String newFileName = UUID.randomUUID().toString() + "." + extension;
        String imagePath = serviceName + uploadDir + "/" + newFileName;
        Path path = Paths.get(imagePath);

        try (OutputStream os = Files.newOutputStream(path, StandardOpenOption.CREATE)) {
            IOUtils.copy(file.getInputStream(), os);
        }

        return new ImageResponse(fileName, uploadDir + "/" + newFileName);

    }


    public ResponseEntity<Resource> serveImage(String fileName, String uploadDir) throws IOException {
        Path imagePath = Paths.get(serviceName + uploadDir + "/" + fileName);
        Resource file = new UrlResource(imagePath.toUri());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                .body(new InputStreamResource(file.getInputStream()));
    }


}
