package com.example.agriecommerce.utils;

import com.example.agriecommerce.entity.Cooperation;
import com.example.agriecommerce.entity.OrderStatus;
import com.example.agriecommerce.repository.CooperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class CooperationScheduler {

    private final CooperationRepository cooperationRepository;
    @Autowired
    public CooperationScheduler(CooperationRepository cooperationRepository) {
        this.cooperationRepository = cooperationRepository;
    }
    @Scheduled(fixedRate = 24*60*60*1000) // 24h
    public void updateCooperationStatus(){
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<Cooperation> cooperationList = cooperationRepository.findByPaymentStatusIsNullAndDateCreatedBefore(sevenDaysAgo);
        for (Cooperation cooperation : cooperationList){
            cooperation.setCooperationStatus(OrderStatus.CANCELLED);
            cooperationRepository.save(cooperation);
        }
    }
}
