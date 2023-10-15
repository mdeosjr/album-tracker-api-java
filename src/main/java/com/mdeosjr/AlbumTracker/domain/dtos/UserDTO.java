package com.mdeosjr.AlbumTracker.domain.dtos;

import java.util.UUID;

public record UserDTO(UUID userId, String name, String email) { }