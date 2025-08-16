package com.marcos.notes.service.implement;

import com.marcos.notes.dto.UserDTO;
import com.marcos.notes.mapper.UserMapper;
import com.marcos.notes.repository.UserRepository;
import com.marcos.notes.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Optional<UserDTO> getById(Long id) {
        return userRepository.findById(id).map(userMapper::toDto);
    }
}
