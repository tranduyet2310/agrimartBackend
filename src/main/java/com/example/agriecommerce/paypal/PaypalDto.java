package com.example.agriecommerce.paypal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaypalDto {
    private double price;
    private String currency;
    private String method;
    private String intent;
    private String description;

}
