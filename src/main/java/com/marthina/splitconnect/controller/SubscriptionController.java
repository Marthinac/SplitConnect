package com.marthina.splitconnect.controller;

import com.marthina.splitconnect.models.Subscription;
import com.marthina.splitconnect.service.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class SubscriptionController {

    private final SubscriptionService subsService;

    public SubscriptionController(SubscriptionService subsService) { this.subsService = subsService; }

    @PostMapping
    public ResponseEntity<Subscription> create (@RequestBody Subscription subscription) {
        Subscription created = subsService.create(subscription);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

}
