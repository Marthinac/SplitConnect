package com.marthina.splitconnect.service;

import com.marthina.splitconnect.dto.ChangeEmailDTO;
import com.marthina.splitconnect.dto.ChangePasswordDTO;
import com.marthina.splitconnect.dto.UserCreateDTO;
import com.marthina.splitconnect.dto.UserResponseDTO;
import com.marthina.splitconnect.exception.EmailAlreadyInUseException;
import com.marthina.splitconnect.exception.InvalidPasswordException;
import com.marthina.splitconnect.exception.UserNotFoundException;
import com.marthina.splitconnect.model.User;
import com.marthina.splitconnect.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDTO create(UserCreateDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())){
            throw new EmailAlreadyInUseException(dto.getEmail());
        }

        User user = new User();
        user.setName(dto.getName());
        user.setCountry(dto.getCountry());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User saved = userRepository.save(user);

        return toResponseDTO(saved);
    }

    public UserResponseDTO findById(Long id) {
        return toResponseDTO(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }

    public List<UserResponseDTO> findAll() {
        List<User> users = userRepository.findAllByOrderByIdAsc();
        List<UserResponseDTO> response = new ArrayList<>();

        for (User user : users) {
            response.add(toResponseDTO(user));
        }

        return response;
        //com steam - return userRepository.findAllByOrderByIdAsc().stream().map(this::toResponseDTO).toList();
    }

    @Transactional
    public UserResponseDTO update(Long id, UserCreateDTO dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        user.setName(dto.getName());
        user.setCountry(dto.getCountry());

        return toResponseDTO(userRepository.save(user));
    }

    // retorn UserEntity for internal service (changePassword)
    public User findEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    public void changeEmail(Long userId, ChangeEmailDTO dto){

        User user = findEntityById(userId);

        if (userRepository.existsByEmail(dto.getNewEmail())){
            throw new EmailAlreadyInUseException(dto.getNewEmail());
        }

        user.setEmail(dto.getNewEmail());
        userRepository.save(user);
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordDTO dto) {

        User user = findEntityById(userId);

        //verify current password
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        //save new password
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) throw new UserNotFoundException(id);

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
