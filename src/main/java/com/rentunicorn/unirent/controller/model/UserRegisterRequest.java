package com.rentunicorn.unirent.controller.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserRegisterRequest(@NotNull String username, @NotNull @Email String email, @NotNull String password) {
}
