package com.rentunicorn.unirent.controller;

import com.rentunicorn.unirent.controller.model.BookingRequest;
import com.rentunicorn.unirent.model.Booking;
import com.rentunicorn.unirent.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @RequestBody BookingRequest request) {
        Booking booking = bookingService.createBooking(request.userId(), request.unicornId(), request.startDate(), request.endDate());
        return ResponseEntity.ok(booking);
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Void> completeBooking(@PathVariable Long id) {
        bookingService.completeBooking(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/fail")
    public ResponseEntity<Void> failBooking(@PathVariable Long id) {
        bookingService.failBooking(id);
        return ResponseEntity.ok().build();
    }
}
