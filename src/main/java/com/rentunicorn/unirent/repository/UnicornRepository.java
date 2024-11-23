package com.rentunicorn.unirent.repository;

import com.rentunicorn.unirent.model.Unicorn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnicornRepository extends JpaRepository<Unicorn, Long> {
    Unicorn findByName(String name);
}
