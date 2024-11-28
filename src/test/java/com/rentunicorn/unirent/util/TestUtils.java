package com.rentunicorn.unirent.util;

import com.rentunicorn.unirent.model.Role;
import com.rentunicorn.unirent.model.Unicorn;
import com.rentunicorn.unirent.model.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Set;

public class TestUtils {
    public static Unicorn createUnicorn(Long id, String name, String color, boolean available) {
        Unicorn unicorn = new Unicorn();
        ReflectionTestUtils.setField(unicorn, "id", id);
        unicorn.setName(name);
        unicorn.setColor(color);
        unicorn.setAvailable(available);
        return unicorn;
    }

    public static User createUser(Long id, String username, String email, String password, Set<Role> roles) {
        User user = new User(username, email, password, roles);
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }
}
