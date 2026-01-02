package com.marthina.splitconnect.service;

import com.marthina.splitconnect.dto.UserCreateDTO;
import com.marthina.splitconnect.dto.UserResponseDTO;
import com.marthina.splitconnect.exception.EmailAlreadyInUseException;
import com.marthina.splitconnect.exception.UserNotFoundException;
import com.marthina.splitconnect.model.User;
import com.marthina.splitconnect.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO create(UserCreateDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())){
            throw new EmailAlreadyInUseException(dto.getEmail());
        }

        User user = new User();
        user.setName(dto.getName());
        user.setCountry(dto.getCountry());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        User saved = userRepository.save(user);

        return toResponseDTO(saved);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User update(Long id, User updated) {
        User user = findById(id);

        user.setName(updated.getName());
        user.setCountry(updated.getCountry());
        //user.setEmail(updated.getEmail());
        //user.setPassword(updated.getPassword());

        return userRepository.save(user);
    }

    //public User updateEmail()

    //public User updatePassword()

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setCountry(user.getCountry());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
