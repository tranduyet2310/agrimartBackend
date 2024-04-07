package com.example.agriecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewStatisticDto {
    private Long totalReviews;
    private BigDecimal averageRating;
    private Long oneStar;
    private Long twoStar;
    private Long threeStar;
    private Long fourStar;
    private Long fiveStar;
}
