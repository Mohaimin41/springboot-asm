package com.sisimpur.library.service;

import com.sisimpur.library.dto.BookSummaryDTO;
import com.sisimpur.library.dto.UserCreateReqDTO;
import com.sisimpur.library.dto.UserDTO;
import com.sisimpur.library.exception.ResourceNotFoundException;
import com.sisimpur.library.model.User;
import com.sisimpur.library.repository.BorrowRepository;
import com.sisimpur.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BorrowRepository borrowRepository;

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

        boolean hasActiveBorrows = user.getBorrows().stream()
                .anyMatch(b -> b.getReturnDate() == null);

        if (hasActiveBorrows) {
            throw new IllegalArgumentException("Cannot delete a user with active borrows.");
        }

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
        List<BookSummaryDTO> borrowedBooks = user.getBorrows().stream()
                .filter(b -> b.getReturnDate() == null)
                .map(b -> new BookSummaryDTO(
                        b.getBook().getId(),
                        b.getBook().getTitle(),
                        b.getBook().getGenre(),
                        b.getBook().getPublishedYear()))
                .toList();

        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                borrowedBooks);
    }
}
