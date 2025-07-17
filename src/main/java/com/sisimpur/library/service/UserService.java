package com.sisimpur.library.service;

import com.sisimpur.library.dto.UserCreateReqDTO;
import com.sisimpur.library.dto.UserDTO;
import com.sisimpur.library.exception.ResourceNotFoundException;
import com.sisimpur.library.model.User;
import com.sisimpur.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return toDTO(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    public UserDTO createUser(UserCreateReqDTO request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        User saved = userRepository.save(user);
        return toDTO(saved);
    }

    public UserDTO updateUser(Long id, UserCreateReqDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        User updated = userRepository.save(user);
        return toDTO(updated);
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }
}
