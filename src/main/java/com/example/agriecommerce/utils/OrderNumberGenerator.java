package com.example.agriecommerce.utils;

import java.util.Random;

public class OrderNumberGenerator {
    private static final String PREFIX = "ORD";

    public static String generateOrderNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(900000000) + 1000000000;
        return PREFIX + randomNumber;
    }
}
