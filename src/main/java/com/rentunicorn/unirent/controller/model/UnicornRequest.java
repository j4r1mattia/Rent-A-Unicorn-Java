package com.rentunicorn.unirent.controller.model;

import jakarta.validation.constraints.NotNull;

public record UnicornRequest(@NotNull String name,@NotNull String color) {
}
