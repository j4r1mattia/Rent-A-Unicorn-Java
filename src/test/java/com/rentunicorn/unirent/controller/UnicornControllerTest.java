package com.rentunicorn.unirent.controller;

import com.rentunicorn.unirent.config.SecurityConfig;
import com.rentunicorn.unirent.model.Unicorn;
import com.rentunicorn.unirent.service.UnicornService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UnicornControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UnicornService unicornService;

    @InjectMocks
    private UnicornController unicornController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(unicornController).build();
    }
    @Test
    void testGetAvailableUnicorns() throws Exception {
        Unicorn unicorn1 = new Unicorn("Sparkle", "Rainbow", true);
        Unicorn unicorn2 = new Unicorn("Twinkle", "Pink", true);

        when(unicornService.getAvailableUnicorns()).thenReturn(Arrays.asList(unicorn1, unicorn2));

        mockMvc.perform(get("/api/unicorns/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Sparkle"))
                .andExpect(jsonPath("$[1].name").value("Twinkle"));
    }

    @Test
    void testRentUnicorn() throws Exception {
        Unicorn unicorn = new Unicorn("Sparkle", "Rainbow", false);

        when(unicornService.rentUnicorn(1L)).thenReturn(unicorn);

        mockMvc.perform(patch("/api/unicorns/1/rent").
                        contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sparkle"))
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    void testReturnUnicorn() throws Exception {
        Unicorn unicorn = new Unicorn("Sparkle", "Rainbow", true);

        when(unicornService.returnUnicorn(1L)).thenReturn(unicorn);

        mockMvc.perform(patch("/api/unicorns/1/return"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sparkle"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void testAddUnicorn() throws Exception {
        Unicorn unicorn = new Unicorn("Twinkle", "Pink", true);

        when(unicornService.addUnicorn("Twinkle", "Pink")).thenReturn(unicorn);

        String unicornJson = """
                {
                  "name": "Twinkle",
                  "color": "Pink",
                  "available": true
                }
                """;

        mockMvc.perform(post("/api/unicorns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(unicornJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Twinkle"))
                .andExpect(jsonPath("$.color").value("Pink"));
    }
}

