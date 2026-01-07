package com.marthina.splitconnect.controller;

import com.marthina.splitconnect.dto.SubscriptionUserDTO;
import com.marthina.splitconnect.service.SubscriptionUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptionuser")
//todo
public class SubscriptionUserController {

    private final SubscriptionUserService subsUserService;

    public SubscriptionUserController(SubscriptionUserService subsUserService) {
        this.subsUserService = subsUserService;
    }

    @PutMapping
    public ResponseEntity<SubscriptionUserDTO> addUser (@RequestBody SubscriptionUserDTO dto){
        SubscriptionUserDTO add = subsUserService.addUser(dto.getSubsId(), dto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(add);
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionUserDTO>> findUsersBySubscription(@PathVariable Long subsid){
        return ResponseEntity.ok(subsUserService.findUsersBySubscription(subsid));
    }

    @DeleteMapping
    public ResponseEntity<Void> removeUser(@PathVariable Long subsid, Long userid){
        subsUserService.removeUser(subsid, userid);
        return ResponseEntity.noContent().build();
    }
}
