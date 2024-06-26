package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.NotificationMessage;
import com.example.agriecommerce.payload.ResultDto;
import com.example.agriecommerce.service.FirebaseMessagingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(name = "REST APIs for push notification")
@RestController
@RequestMapping("api/notification")
public class NotificationController {
    private final FirebaseMessagingService firebaseMessagingService;
    @Autowired
    public NotificationController(FirebaseMessagingService firebaseMessagingService) {
        this.firebaseMessagingService = firebaseMessagingService;
    }

    @PostMapping
    public ResponseEntity<ResultDto> sendNotification(@RequestBody NotificationMessage notificationMessage){
        return ResponseEntity.ok(firebaseMessagingService.sendNotificationByToken(notificationMessage));
    }
}
