package com.ossovita.userservice.business.concretes;

import com.ossovita.userservice.core.entities.BusinessPosition;
import com.ossovita.userservice.business.abstracts.BusinessPositionService;
import com.ossovita.userservice.core.dataAccess.BusinessPositionRepository;
import org.springframework.stereotype.Service;

@Service
public class BusinessPositionManager implements BusinessPositionService {

    BusinessPositionRepository businessPositionRepository;

    public BusinessPositionManager(BusinessPositionRepository businessPositionRepository) {
        this.businessPositionRepository = businessPositionRepository;
    }

    @Override
    //@PreAuthorize("hasAuthority('Admin')")
    public BusinessPosition createBusinessPosition(BusinessPosition businessPosition) {
        return businessPositionRepository.save(businessPosition);
    }
}
