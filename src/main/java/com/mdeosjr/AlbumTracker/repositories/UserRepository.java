package com.mdeosjr.AlbumTracker.repositories;

import com.mdeosjr.AlbumTracker.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> { }