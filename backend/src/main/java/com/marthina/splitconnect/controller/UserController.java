package com.marthina.splitconnect.controller;

import com.marthina.splitconnect.dto.ChangeEmailDTO;
import com.marthina.splitconnect.dto.ChangePasswordDTO;
import com.marthina.splitconnect.dto.UserCreateDTO;
import com.marthina.splitconnect.dto.UserResponseDTO;
import com.marthina.splitconnect.exception.UserNotAuthorizedException;
import com.marthina.splitconnect.security.auth.UserPrincipal;
import com.marthina.splitconnect.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create (
            @Valid @RequestBody UserCreateDTO dto){

        UserResponseDTO created = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(
            @PathVariable Long id){

        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> findAll(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {

        return ResponseEntity.ok(userService.findAll(pageable));
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid UserCreateDTO dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal){

        if (!userPrincipal.getId().equals(id)) {
            throw new UserNotAuthorizedException(id, userPrincipal.getId());
        }

        return ResponseEntity.ok(userService.update(id, dto));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestBody @Valid ChangePasswordDTO dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (!userPrincipal.getId().equals(id)) {
            throw new UserNotAuthorizedException(id, userPrincipal.getId());
        }

        userService.changePassword(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/email")
    public ResponseEntity<Void> changeEmail(
            @PathVariable Long id,
            @RequestBody @Valid ChangeEmailDTO dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (!userPrincipal.getId().equals(id)) {
            throw new UserNotAuthorizedException(id, userPrincipal.getId());
        }

        userService.changeEmail(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal){

        if (!userPrincipal.getId().equals(id)) {
            throw new UserNotAuthorizedException(id, userPrincipal.getId());
        }

        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(
            @AuthenticationPrincipal UserPrincipal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(userService.findById(principal.getId()));
    }
}
