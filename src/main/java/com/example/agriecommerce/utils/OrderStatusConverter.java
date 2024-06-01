package com.example.agriecommerce.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

@Component
@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(OrderStatus attribute) {
        return (Integer) attribute.ordinal();
    }

    @Override
    public OrderStatus convertToEntityAttribute(Integer dbData) {
        return OrderStatus.values()[dbData];
    }
}
