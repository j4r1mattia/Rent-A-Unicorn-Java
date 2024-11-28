package com.rentunicorn.unirent.controller.model;

import jakarta.validation.constraints.NotNull;

public record UserLoginRequest(@NotNull String username, @NotNull String password) {
}
