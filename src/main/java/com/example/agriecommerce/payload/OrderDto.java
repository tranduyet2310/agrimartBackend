package com.example.agriecommerce.payload;

import com.example.agriecommerce.utils.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private String orderNumber;
    private OrderStatus orderStatus;
    private String paymentMethod;
    private String paymentStatus;
    private Long total;
    private Long userId;
    private Long addressId;
    private List<OrderDetailDto> orderDetails;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dateCreated;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dateUpdated;
}
