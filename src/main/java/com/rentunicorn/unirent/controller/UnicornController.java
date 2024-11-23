package com.rentunicorn.unirent.controller;

import com.rentunicorn.unirent.controller.model.UnicornRequest;
import com.rentunicorn.unirent.model.Unicorn;
import com.rentunicorn.unirent.service.UnicornService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unicorns")
public class UnicornController {

    @Autowired
    private UnicornService unicornService;

    @GetMapping("/available")
    public List<Unicorn> getAvailableUnicorns() {
        return unicornService.getAvailableUnicorns();
    }

    @PostMapping
    public Unicorn addUnicorn(@RequestBody UnicornRequest unicorn) {
        return unicornService.addUnicorn(unicorn.name(), unicorn.color());
    }

    @PatchMapping("/{id}/rent")
    public Unicorn rentUnicorn(@PathVariable Long id) {
        return unicornService.rentUnicorn(id);
    }

    @PatchMapping("{id}/return")
    public Unicorn returnUnicorn(@PathVariable Long id) {
        return unicornService.returnUnicorn(id);
    }
}

