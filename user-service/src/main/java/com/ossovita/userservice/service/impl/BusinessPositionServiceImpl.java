package com.ossovita.userservice.service.impl;

import com.ossovita.userservice.entity.BusinessPosition;
import com.ossovita.userservice.service.BusinessPositionService;
import com.ossovita.userservice.repository.BusinessPositionRepository;
import org.springframework.stereotype.Service;

@Service
public class BusinessPositionServiceImpl implements BusinessPositionService {

    BusinessPositionRepository businessPositionRepository;

    public BusinessPositionServiceImpl(BusinessPositionRepository businessPositionRepository) {
        this.businessPositionRepository = businessPositionRepository;
    }

    @Override
    //@PreAuthorize("hasAuthority('Admin')")
    public BusinessPosition createBusinessPosition(BusinessPosition businessPosition) {
        return businessPositionRepository.save(businessPosition);
    }
}
