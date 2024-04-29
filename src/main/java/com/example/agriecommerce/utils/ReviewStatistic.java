package com.example.agriecommerce.utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ReviewStatistic {
    Long getId();
    String getFeedBack();
    BigDecimal getRating();
    LocalDateTime getReviewDate();
    Long getProductId();
    String getFullName();
    String getProductName();
}
