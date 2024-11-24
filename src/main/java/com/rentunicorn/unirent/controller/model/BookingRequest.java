package com.rentunicorn.unirent.controller.model;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BookingRequest(@NotNull Long userId,@NotNull Long unicornId,@NotNull LocalDate startDate,@NotNull LocalDate endDate) {
}
