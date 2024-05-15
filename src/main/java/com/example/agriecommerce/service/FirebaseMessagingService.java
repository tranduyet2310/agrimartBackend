package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.NotificationMessage;
import com.example.agriecommerce.payload.ResultDto;

public interface FirebaseMessagingService {
    ResultDto sendNotificationByToken(NotificationMessage notificationMessage);
}
