package com.marthina.splitconnect.controller;

import com.marthina.splitconnect.dto.SubscriptionUserDTO;
import com.marthina.splitconnect.security.auth.UserPrincipal;
import com.marthina.splitconnect.service.SubscriptionUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscription/{subscriptionId}/users")
public class SubscriptionUserController {

    private final SubscriptionUserService subsUserService;

    public SubscriptionUserController(SubscriptionUserService subsUserService) {
        this.subsUserService = subsUserService;
    }

    @PostMapping("/request")
    public ResponseEntity<SubscriptionUserDTO> requestJoin(
            @PathVariable Long subscriptionId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {  // usuário que quer entrar

        SubscriptionUserDTO result = subsUserService
                .requestJoin(subscriptionId, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/{subscriptionUserId}/approve")
    public ResponseEntity<SubscriptionUserDTO> approveJoin(
            @PathVariable Long subscriptionUserId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        SubscriptionUserDTO result = subsUserService
                .approveJoin(subscriptionUserId, userPrincipal.getId());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/pending/{subscriptionUserId}/reject")
    public ResponseEntity<SubscriptionUserDTO> rejectJoin(
            @PathVariable Long subscriptionUserId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        SubscriptionUserDTO result = subsUserService
                .rejectJoin(subscriptionUserId, userPrincipal.getId());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<SubscriptionUserDTO>> listPendingRequests(
            @PathVariable Long subscriptionId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        List<SubscriptionUserDTO> requests = subsUserService
                .listPendingRequests(subscriptionId, userPrincipal.getId());
        return ResponseEntity.ok(requests);
    }

    @PostMapping
    public ResponseEntity<SubscriptionUserDTO> addDirectUser(
            @PathVariable Long subscriptionId,
            @RequestBody @Valid SubscriptionUserDTO dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        SubscriptionUserDTO added =
                subsUserService.addDirectUser(subscriptionId, dto, userPrincipal.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(added);
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionUserDTO>> findUsersBySubscription(
            @PathVariable Long subscriptionId) {

        return ResponseEntity.ok(
                subsUserService.findUsersBySubscription(subscriptionId)
        );
    }

    @PatchMapping("/owner/{newOwnerId}")
    public ResponseEntity<Void> changeOwner(
            @PathVariable Long subscriptionId,
            @PathVariable Long newOwnerId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long currentOwnerId = userPrincipal.getId();
        subsUserService.changeOwner(subscriptionId, newOwnerId, currentOwnerId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{targetUserId}")
    public ResponseEntity<Void> removeParticipant(
            @PathVariable Long subscriptionId,
            @PathVariable Long targetUserId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long actionUserId = userPrincipal.getId();

        subsUserService.removeParticipant(subscriptionId, actionUserId, targetUserId);
        return ResponseEntity.noContent().build();
    }

}
