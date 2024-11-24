package com.rentunicorn.unirent.service;

import com.rentunicorn.unirent.model.Booking;
import com.rentunicorn.unirent.model.Unicorn;
import com.rentunicorn.unirent.model.BookingStatus;
import com.rentunicorn.unirent.repository.BookingRepository;
import com.rentunicorn.unirent.repository.UnicornRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UnicornRepository unicornRepository;

    public BookingService(BookingRepository bookingRepository, UnicornRepository unicornRepository) {
        this.bookingRepository = bookingRepository;
        this.unicornRepository = unicornRepository;
    }

    public Booking createBooking(Long userId, Long unicornId, LocalDate startDate, LocalDate endDate) {
        Unicorn unicorn = unicornRepository.findById(unicornId)
                .orElseThrow(() -> new IllegalArgumentException("Unicorn not found"));

        if (!unicorn.isAvailable()) {
            throw new IllegalStateException("Unicorn is not available");
        }

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setUnicornId(unicornId);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setStatus(BookingStatus.PENDING);

        unicorn.setAvailable(false);
        unicornRepository.save(unicorn);

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }

    public void cancelBooking(Long id) {
        Booking booking = getBookingById(id);
        booking.setStatus(BookingStatus.CANCELLED);

        Unicorn unicorn = unicornRepository.findById(booking.getUnicornId())
                .orElseThrow(() -> new IllegalArgumentException("Unicorn not found"));

        unicorn.setAvailable(true);
        unicornRepository.save(unicorn);

        bookingRepository.save(booking);
    }

    public void completeBooking(Long id) {
        Booking booking = getBookingById(id);
        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            booking.setStatus(BookingStatus.COMPLETED); // Impostiamo lo stato a COMPLETED

            Unicorn unicorn = unicornRepository.findById(booking.getUnicornId())
                    .orElseThrow(() -> new IllegalArgumentException("Unicorn not found"));

            unicorn.setAvailable(true); // Ripristina la disponibilità dell'unicorno
            unicornRepository.save(unicorn);

            bookingRepository.save(booking);
        } else {
            throw new IllegalStateException("Booking must be confirmed before completion");
        }
    }

    // Metodo per segnare una prenotazione come fallita
    public void failBooking(Long id) {
        Booking booking = getBookingById(id);
        booking.setStatus(BookingStatus.FAILED);

        Unicorn unicorn = unicornRepository.findById(booking.getUnicornId())
                .orElseThrow(() -> new IllegalArgumentException("Unicorn not found"));

        unicorn.setAvailable(true); // Ripristina la disponibilità dell'unicorno
        unicornRepository.save(unicorn);

        bookingRepository.save(booking);
    }
}
