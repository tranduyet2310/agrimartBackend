package com.example.agriecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {
    private String recipientToken;
    private String title;
    private String body;
    private Map<String, String> data;
}
