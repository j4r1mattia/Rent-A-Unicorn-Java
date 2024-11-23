package com.rentunicorn.unirent.service;

import com.rentunicorn.unirent.model.Unicorn;
import com.rentunicorn.unirent.repository.UnicornRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnicornService {

    @Autowired
    private UnicornRepository unicornRepository;

    public List<Unicorn> getAvailableUnicorns() {
        return unicornRepository.findAll().stream()
                .filter(Unicorn::isAvailable)
                .toList();
    }

    public Unicorn rentUnicorn(Long id) {
        Optional<Unicorn> unicornOptional = unicornRepository.findById(id);
        if (unicornOptional.isPresent()) {
            Unicorn unicorn = unicornOptional.get();
            if (!unicorn.isAvailable()) {
                throw new IllegalStateException("Unicorn is already rented!");
            }
            unicorn.setAvailable(false);
            return unicornRepository.save(unicorn);
        } else {
            throw new IllegalArgumentException("Unicorn not found!");
        }
    }

    public Unicorn addUnicorn(String name, String color) {
        Unicorn unicorn = new Unicorn(name, color, true);
        return unicornRepository.save(unicorn);
    }

    public Unicorn returnUnicorn(Long id) {
        Optional<Unicorn> unicornOptional = unicornRepository.findById(id);
        if (unicornOptional.isPresent()) {
            Unicorn unicorn = unicornOptional.get();
            unicorn.setAvailable(true);
            return unicornRepository.save(unicorn);
        } else {
            throw new IllegalArgumentException("Unicorn not found!");
        }
    }
}

