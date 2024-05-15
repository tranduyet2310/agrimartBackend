package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.payload.NotificationMessage;
import com.example.agriecommerce.payload.ResultDto;
import com.example.agriecommerce.service.FirebaseMessagingService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessagingServiceImpl implements FirebaseMessagingService {
    private final FirebaseMessaging firebaseMessaging;

    @Autowired
    public FirebaseMessagingServiceImpl(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    @Override
    public ResultDto sendNotificationByToken(NotificationMessage notificationMessage) {
        Notification notification = Notification.builder().setTitle(notificationMessage.getTitle())
                .setBody(notificationMessage.getBody()).build();

        Message message = Message.builder()
                .setToken(notificationMessage.getRecipientToken())
                .setNotification(notification)
                .putAllData(notificationMessage.getData()).build();

        ResultDto resultDto = new ResultDto();
        try {
            firebaseMessaging.send(message);
            resultDto.setMessage("Send notification successfully");
            resultDto.setSuccessful(true);
            return resultDto;
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            resultDto.setMessage("Error sending notification");
            resultDto.setSuccessful(true);
            return resultDto;
        }
    }
}
