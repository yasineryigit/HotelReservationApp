package com.ossovita.accountingservice.core.dataAccess;

import com.ossovita.accountingservice.core.entities.ReservationPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationPaymentRepository extends JpaRepository<ReservationPayment, Long> {


}
