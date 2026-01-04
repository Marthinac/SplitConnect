package com.marthina.splitconnect.controller;

import com.marthina.splitconnect.dto.SubscriptionDTO;
import com.marthina.splitconnect.model.Subscription;
import com.marthina.splitconnect.service.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<SubscriptionDTO> create (@RequestBody SubscriptionDTO dto) {
        SubscriptionDTO created = subsService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(subsService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionDTO>> findAll() {
        return ResponseEntity.ok(subsService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> update(@PathVariable Long id, @RequestBody SubscriptionDTO dto){
        return ResponseEntity.ok(subsService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        subsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
