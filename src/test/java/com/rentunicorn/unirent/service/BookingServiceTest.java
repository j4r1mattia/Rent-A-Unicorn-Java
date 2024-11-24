package com.rentunicorn.unirent.service;

import com.rentunicorn.unirent.model.Booking;
import com.rentunicorn.unirent.model.BookingStatus;
import com.rentunicorn.unirent.model.Unicorn;
import com.rentunicorn.unirent.repository.BookingRepository;
import com.rentunicorn.unirent.repository.UnicornRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import  com.rentunicorn.unirent.util.TestUtils;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UnicornRepository unicornRepository;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBooking_Success() {
        // Arrange
        Unicorn unicorn = TestUtils.createUnicorn(1L,"Rainbow", "Blue", true);
        Booking booking = new Booking();
        booking.setUserId(42L);
        booking.setUnicornId(unicorn.getId());
        booking.setStartDate(LocalDate.now());
        booking.setEndDate(LocalDate.now().plusDays(1));

        when(unicornRepository.findById(1L)).thenReturn(Optional.of(unicorn));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // Act
        Booking createdBooking = bookingService.createBooking(42L, 1L, LocalDate.now(), LocalDate.now().plusDays(1));

        // Assert
        assertNotNull(createdBooking);
        assertEquals(42L, createdBooking.getUserId());
        assertEquals(LocalDate.now(), booking.getStartDate());
        assertEquals(LocalDate.now().plusDays(1), booking.getEndDate());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testCreateBooking_UnicornNotAvailable() {
        // Arrange
        Unicorn unicorn = TestUtils.createUnicorn(1L, "Rainbow", "Blue", false);

        when(unicornRepository.findById(1L)).thenReturn(Optional.of(unicorn));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                bookingService.createBooking(42L, 1L, LocalDate.now(), LocalDate.now().plusDays(1))
        );

        assertEquals("Unicorn is not available", exception.getMessage());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void testCompleteBooking_Success() {
        // Arrange
        Unicorn unicorn = TestUtils.createUnicorn(1L, "Rainbow", "Blue", false);

        Booking booking = new Booking();
        booking.setUnicornId(1L);
        booking.setStatus(BookingStatus.CONFIRMED);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(unicornRepository.findById(1L)).thenReturn(Optional.of(unicorn));

        // Act
        bookingService.completeBooking(1L);

        // Assert
        assertEquals(BookingStatus.COMPLETED, booking.getStatus());
        assertTrue(unicorn.isAvailable());
        verify(bookingRepository, times(1)).save(booking);
        verify(unicornRepository, times(1)).save(unicorn);
    }

    @Test
    void testCompleteBooking_FailureWhenNotConfirmed() {
        // Arrange
        Booking booking = new Booking();

        booking.setStatus(BookingStatus.PENDING); // Not confirmed

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                bookingService.completeBooking(1L)
        );

        assertEquals("Booking must be confirmed before completion", exception.getMessage());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void testFailBooking_Success() {
        // Arrange
        Unicorn unicorn = TestUtils.createUnicorn(1L, "Rainbow", "Blue", true);

        Booking booking = new Booking();
        booking.setUnicornId(1L);
        booking.setStatus(BookingStatus.PENDING);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(unicornRepository.findById(1L)).thenReturn(Optional.of(unicorn));

        // Act
        bookingService.failBooking(1L);

        // Assert
        assertEquals(BookingStatus.FAILED, booking.getStatus());
        assertTrue(unicorn.isAvailable());
        verify(bookingRepository, times(1)).save(booking);
        verify(unicornRepository, times(1)).save(unicorn);
    }

    @Test
    void testFailBooking_BookingNotFound() {
        // Arrange
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                bookingService.failBooking(1L)
        );

        assertEquals("Booking not found", exception.getMessage());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void testCancelBooking_Success() {
        // Arrange
        Unicorn unicorn = TestUtils.createUnicorn(1L, "Rainbow", "Blue", false);

        Booking booking = new Booking();
        booking.setUnicornId(1L);
        booking.setStatus(BookingStatus.PENDING);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(unicornRepository.findById(1L)).thenReturn(Optional.of(unicorn));

        // Act
        bookingService.cancelBooking(1L);

        // Assert
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        assertTrue(unicorn.isAvailable());
        verify(bookingRepository, times(1)).save(booking);
        verify(unicornRepository, times(1)).save(unicorn);
    }
}
