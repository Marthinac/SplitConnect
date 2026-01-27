package com.marthina.splitconnect.controller;

import com.marthina.splitconnect.dto.AvailableSubscriptionDTO;
import com.marthina.splitconnect.dto.SubscriptionDTO;
import com.marthina.splitconnect.model.enums.Country;
import com.marthina.splitconnect.model.enums.ServicesType;
import com.marthina.splitconnect.security.auth.UserPrincipal;
import com.marthina.splitconnect.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subsService;

    public SubscriptionController(SubscriptionService subsService) {
        this.subsService = subsService;
    }

    @PostMapping
    public ResponseEntity<SubscriptionDTO> create (
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody @Valid SubscriptionDTO dto) {

        SubscriptionDTO created = subsService.create(userPrincipal.getId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> findById(
            @PathVariable Long id){

        return ResponseEntity.ok(subsService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<SubscriptionDTO>> findAll(
            @PageableDefault(size = 10, sort = "serviceName") Pageable pageable) {

        return ResponseEntity.ok(subsService.findAll(pageable));
    }

    @GetMapping("/available")
    public ResponseEntity<Page<AvailableSubscriptionDTO>> findAvailable(
            @PageableDefault(size = 10, sort = "pricePerUser,asc") Pageable pageable,
            @RequestParam(required = false) Country country,
            @RequestParam(required = false) ServicesType serviceType) {

        Page<AvailableSubscriptionDTO> available = subsService.
                findAvailable(pageable, country, serviceType);

        return ResponseEntity.ok(available);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<SubscriptionDTO>> findFiltered(
            @RequestParam(required = false) Country country,
            @RequestParam(required = false) ServicesType serviceType,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean hasVacancy,
            @RequestParam(required = false) String serviceName,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<SubscriptionDTO> result = subsService.findFiltered(
                country, serviceType, maxPrice, hasVacancy, serviceName, pageable);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> update(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody @Valid SubscriptionDTO dto){

        Long ownerId = userPrincipal.getId();
        SubscriptionDTO result = subsService.update(id, ownerId, dto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal){

        subsService.cancel(id, userPrincipal.getId());
        return ResponseEntity.noContent().build();
    }
}
