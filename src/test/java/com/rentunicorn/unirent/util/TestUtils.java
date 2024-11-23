package com.rentunicorn.unirent.util;

import com.rentunicorn.unirent.model.Unicorn;
import org.springframework.test.util.ReflectionTestUtils;

public class TestUtils {
    public static Unicorn createUnicorn(Long id, String name, String color, boolean available) {
        Unicorn unicorn = new Unicorn();
        ReflectionTestUtils.setField(unicorn, "id", id); // Usa Reflection per impostare il campo privato
        unicorn.setName(name);
        unicorn.setColor(color);
        unicorn.setAvailable(available);
        return unicorn;
    }
}
