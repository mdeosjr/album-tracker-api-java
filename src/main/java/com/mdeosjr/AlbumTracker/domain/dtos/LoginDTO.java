package com.mdeosjr.AlbumTracker.domain.dtos;

import jakarta.validation.constraints.NotNull;

public record LoginDTO(@NotNull String email, @NotNull String password) { }