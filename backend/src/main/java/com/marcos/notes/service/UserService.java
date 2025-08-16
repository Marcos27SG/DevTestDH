package com.marcos.notes.service;

import com.marcos.notes.dto.UserDTO;

import java.util.Optional;

public interface UserService {
    Optional<UserDTO> getById(Long id);
}
