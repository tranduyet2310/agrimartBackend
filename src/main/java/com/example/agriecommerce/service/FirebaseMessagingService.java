package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.NotificationMessage;

public interface FirebaseMessagingService {
    String sendNotificationByToken(NotificationMessage notificationMessage);
}
