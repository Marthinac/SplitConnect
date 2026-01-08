package com.marthina.splitconnect.controller;

import com.marthina.splitconnect.dto.SubscriptionUserDTO;
import com.marthina.splitconnect.service.SubscriptionUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptions/{subscriptionId}/users")
public class SubscriptionUserController {

    private final SubscriptionUserService subsUserService;

    public SubscriptionUserController(SubscriptionUserService subsUserService) {
        this.subsUserService = subsUserService;
    }

    @PostMapping
    public ResponseEntity<SubscriptionUserDTO> addUser(
            @PathVariable Long subscriptionId,
            @RequestBody SubscriptionUserDTO dto) {

        SubscriptionUserDTO added =
                subsUserService.addUser(subscriptionId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(added);
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionUserDTO>> findUsersBySubscription(@PathVariable Long subscriptionId) {
        return ResponseEntity.ok(
                subsUserService.findUsersBySubscription(subscriptionId)
        );
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeUser(
            @PathVariable Long subscriptionId,
            @PathVariable Long userId) {

        subsUserService.removeUser(subscriptionId, userId);
        return ResponseEntity.noContent().build();
    }

}
