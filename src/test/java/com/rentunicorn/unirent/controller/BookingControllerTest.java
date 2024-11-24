package com.rentunicorn.unirent.controller;

import com.rentunicorn.unirent.model.Booking;
import com.rentunicorn.unirent.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    @Test
    void testCreateBooking_Success() throws Exception {
        // Arrange
        Booking booking = new Booking();
        booking.setUserId(42L);
        booking.setStartDate(LocalDate.now());
        booking.setEndDate(LocalDate.now().plusDays(1));

        when(bookingService.createBooking(anyLong(), anyLong(), any(), any())).thenReturn(booking);

        // Act & Assert
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "userId": 42,
                          "unicornId": 1,
                          "startDate": "2024-11-24",
                          "endDate": "2024-11-25"
                        }
                        """))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(42));
    }

    @Test
    void testGetAllBookings_Success() throws Exception {
        // Arrange
        when(bookingService.getAllBookings()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testCancelBooking_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(patch("/api/bookings/1/cancel"))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).cancelBooking(1L);
    }

    @Test
    void testCompleteBooking_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(patch("/api/bookings/1/complete"))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).completeBooking(1L);
    }

    @Test
    void testFailBooking_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(patch("/api/bookings/1/fail"))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).failBooking(1L);
    }
}
