package com.rentunicorn.unirent.controller.model;

import java.time.LocalDate;

public record BookingRequest(Long userId, Long unicornId, LocalDate startDate,LocalDate endDate) {
}
