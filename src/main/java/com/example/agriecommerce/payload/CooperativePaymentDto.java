package com.example.agriecommerce.payload;

import com.example.agriecommerce.utils.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CooperativePaymentDto {
    private Long id;
    private String orderNumber;
    private OrderStatus orderStatus;
    private String paymentMethod;
    private String paymentStatus;
    private Long total;
    private Long userId;
    private Long supplierId;
    private Long cooperationId;
    @JsonProperty("cropsName")
    private String cooperationFieldCropsName;
    @JsonProperty("requireYield")
    private Double cooperationRequireYield;
    private String supplierContactName;
    private String userFullName;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dateCreated;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dateUpdated;
}
