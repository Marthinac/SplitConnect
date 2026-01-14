package com.marthina.splitconnect.service;

import com.marthina.splitconnect.dto.ChangePasswordDTO;
import com.marthina.splitconnect.dto.UserCreateDTO;
import com.marthina.splitconnect.dto.UserResponseDTO;
import com.marthina.splitconnect.exception.EmailAlreadyInUseException;
import com.marthina.splitconnect.model.User;
import com.marthina.splitconnect.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;


    @Test
    void shouldCreateUserSuccessfully() {

        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail("test@email.com");
        dto.setPassword("rawPassword");
        dto.setName("Test User");
        dto.setCountry("BR");

        //test email
        when(userRepository.existsByEmail(dto.getEmail()))
                .thenReturn(false);

        //password not save as string, but with encode
        when(passwordEncoder.encode("rawPassword"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserResponseDTO response = userService.create(dto);

        // Assert - verify
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(dto.getEmail());

        verify(passwordEncoder).encode("rawPassword");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail("test@email.com");

        when(userRepository.existsByEmail(dto.getEmail()))
                .thenReturn(true);

        assertThatThrownBy(() -> userService.create(dto))
                .isInstanceOf(EmailAlreadyInUseException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldChangePasswordSuccessfully() {

        ChangePasswordDTO dto = new ChangePasswordDTO();
        dto.setCurrentPassword("old");
        dto.setNewPassword("new");

        User user = new User();
        user.setId(1L);
        user.setEmail("test@email.com");
        user.setPassword("encodedOld");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("old", "encodedOld"))
                .thenReturn(true);

        when(passwordEncoder.encode("new"))
                .thenReturn("encodedNew");

        // Act
        userService.changePassword(1L, dto);

        // Assert
        assertThat(user.getPassword()).isEqualTo("encodedNew");

        verify(passwordEncoder).matches("old", "encodedOld");
        verify(passwordEncoder).encode("new");
        verify(userRepository).save(user);
    }




}