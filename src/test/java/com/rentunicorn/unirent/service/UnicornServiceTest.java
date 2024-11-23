package com.rentunicorn.unirent.service;

import com.rentunicorn.unirent.model.Unicorn;
import com.rentunicorn.unirent.repository.UnicornRepository;
import com.rentunicorn.unirent.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UnicornServiceTest {

    @Mock
    private UnicornRepository unicornRepository;

    @InjectMocks
    private UnicornService unicornService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAvailableUnicorns() {
        Unicorn unicorn1 = new Unicorn("Sparkle", "Rainbow", true);
        Unicorn unicorn2 = new Unicorn("Twinkle", "Pink", true);
        Unicorn unicorn3 = new Unicorn("Glitter", "Gold", false);

        when(unicornRepository.findAll()).thenReturn(Arrays.asList(unicorn1, unicorn2, unicorn3));

        List<Unicorn> availableUnicorns = unicornService.getAvailableUnicorns();

        assertEquals(2, availableUnicorns.size());
        assertTrue(availableUnicorns.contains(unicorn1));
        assertTrue(availableUnicorns.contains(unicorn2));
    }

    @Test
    void testRentUnicorn_Success() {
        Unicorn unicorn = TestUtils.createUnicorn(1L, "Rainbow", "Blue", true);

        when(unicornRepository.findById(1L)).thenReturn(Optional.of(unicorn));
        when(unicornRepository.save(any(Unicorn.class))).thenReturn(unicorn);

        Unicorn rentedUnicorn = unicornService.rentUnicorn(1L);

        assertFalse(rentedUnicorn.isAvailable());
        verify(unicornRepository, times(1)).save(unicorn);
    }

    @Test
    void testRentUnicorn_Failure_UnicornNotAvailable() {
        Unicorn unicorn = TestUtils.createUnicorn(1L, "Rainbow", "Blue", false);

        when(unicornRepository.findById(1L)).thenReturn(Optional.of(unicorn));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            unicornService.rentUnicorn(1L);
        });

        assertEquals("Unicorn is already rented!", exception.getMessage());
        verify(unicornRepository, never()).save(any(Unicorn.class));
    }

    @Test
    void testAddUnicorn() {
        Unicorn unicorn = new Unicorn("Twinkle", "Pink", true);

        when(unicornRepository.save(any(Unicorn.class))).thenReturn(unicorn);

        Unicorn savedUnicorn = unicornService.addUnicorn("Twinkle", "Pink");

        assertNotNull(savedUnicorn);
        assertEquals("Twinkle", savedUnicorn.getName());
        assertEquals("Pink", savedUnicorn.getColor());
        assertTrue(savedUnicorn.isAvailable());
    }
}

