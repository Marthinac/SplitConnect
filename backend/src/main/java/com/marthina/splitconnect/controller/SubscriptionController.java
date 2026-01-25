package com.marthina.splitconnect.controller;

import com.marthina.splitconnect.dto.AvailableSubscriptionDTO;
import com.marthina.splitconnect.dto.SubscriptionDTO;
import com.marthina.splitconnect.model.enums.Country;
import com.marthina.splitconnect.model.enums.ServicesType;
import com.marthina.splitconnect.security.auth.UserPrincipal;
import com.marthina.splitconnect.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    private final SubscriptionService subsService;

    public SubscriptionController(SubscriptionService subsService) {
        this.subsService = subsService;
    }

    @PostMapping
    public ResponseEntity<SubscriptionDTO> create (@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid SubscriptionDTO dto) {
        SubscriptionDTO created = subsService.create(userPrincipal.getId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(subsService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<SubscriptionDTO>> findAll(
            @PageableDefault(size = 10, sort = "dateStart", direction = Sort.Direction.DESC)
            Pageable pageable) {

        return ResponseEntity.ok(subsService.findAll(pageable));
    }


    @GetMapping("/available")
    public ResponseEntity<Page<AvailableSubscriptionDTO>> findAvailableSubscriptions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Country country,
            @RequestParam(required = false) ServicesType serviceType) {

        Pageable pageable = PageRequest.of(page, size);
        Page<AvailableSubscriptionDTO> available = subsService.findAvailableSubscriptions(
                pageable, country, serviceType);

        return ResponseEntity.ok(available);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> update(@PathVariable Long id, @RequestBody SubscriptionDTO dto){
        return ResponseEntity.ok(subsService.update(id, dto));
    }

    @DeleteMapping("/{id}/{ownerId}")
    public ResponseEntity<Void> cancel(@PathVariable Long id, @PathVariable Long ownerId){
        subsService.cancel(id, ownerId);
        return ResponseEntity.noContent().build();
    }
}
