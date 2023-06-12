package com.ossovita.reservationservice.util;

import com.ossovita.commonservice.enums.ReservationStatus;
import com.ossovita.reservationservice.entity.Reservation;
import com.ossovita.reservationservice.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class ScheduledJob {

    private static final int MAX_RETRY_COUNT = 3;
    private static final Duration RETRY_DELAY = Duration.ofMinutes(5);

    ReservationService reservationService;

    public ScheduledJob(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Scheduled(cron = "0 0 7,15 * * ?") // works at 7:00am and 3:00pm everyday
    public void processJob() {
        int retryCount = 0;
        boolean success = false;

        while (retryCount < MAX_RETRY_COUNT && !success) {
            try {
                List<Reservation> reservationList = reservationService.getAllReservations();
                List<Reservation> processedReservationList = reservationList.stream().map(this::processReservation).toList();//process
                reservationService.saveAll(processedReservationList);
                success = true;
            } catch (Exception e) {
                retryCount++;
                if (retryCount < MAX_RETRY_COUNT) {
                    // Belirli bir süre sonra yeniden deneme
                    try {
                        Thread.sleep(RETRY_DELAY.toMillis());
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    //max retry count reached
                    log.error("Scheduled job failed after maximum retries: {}", e.getMessage());
                }
            }
        }
    }

    private Reservation processReservation(Reservation reservation) {//set reservation status as expired depends on the reservation end time
        boolean isExpired = reservation.getReservationEndTime().isEqual(LocalDateTime.now()) || reservation.getReservationEndTime().isAfter(LocalDateTime.now());

        if (reservation.getReservationStatus().equals(ReservationStatus.BOOKED) && (isExpired)) {//if reservation is expired
            reservation.setReservationStatus(ReservationStatus.EXPIRED);
            //TODO: send notification to the customer (stayover offer, review instruction)
        }
        return reservation;
    }
}
