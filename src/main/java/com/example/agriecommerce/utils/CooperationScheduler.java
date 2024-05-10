package com.example.agriecommerce.utils;

import com.example.agriecommerce.entity.Cooperation;
import com.example.agriecommerce.entity.OrderStatus;
import com.example.agriecommerce.entity.User;
import com.example.agriecommerce.payload.NotificationMessage;
import com.example.agriecommerce.repository.CooperationRepository;
import com.example.agriecommerce.service.FirebaseMessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CooperationScheduler {

    private final CooperationRepository cooperationRepository;
    private final FirebaseMessagingService firebaseMessagingService;

    @Autowired
    public CooperationScheduler(CooperationRepository cooperationRepository,
                                FirebaseMessagingService firebaseMessagingService) {
        this.cooperationRepository = cooperationRepository;
        this.firebaseMessagingService = firebaseMessagingService;
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // 24h
    public void updateCooperationStatus() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<Cooperation> cooperationList = cooperationRepository.findByPaymentStatusIsNullAndDateCreatedBefore(sevenDaysAgo);
        for (Cooperation cooperation : cooperationList) {
            String dateCreated = DateTimeUtil.formatDate(cooperation.getDateCreated());
            String content = "Đơn hàng: " + cooperation.getCropsName() + " - Số lượng: " +estimateYield(cooperation.getRequireYield());

            cooperation.setCooperationStatus(OrderStatus.CANCELLED);
            cooperationRepository.save(cooperation);
            // send notification to user
            User currentUser = cooperation.getUser();
            String fcmToken = currentUser.getFcmToken();
            if (fcmToken != null) {
                // Prepare notification message
                NotificationMessage message = new NotificationMessage();
                message.setTitle("Thông báo");
                String messageContent = "Đơn hàng của bạn bị hủy do không thực hiện thanh toán đúng hạn sau 7 ngày!" +
                        " \n" + "Ngày đặt: " + dateCreated +
                        " \n" + content;
                message.setBody(messageContent);
                message.setRecipientToken(fcmToken);

                Map<String, String> data = new HashMap<>();
                data.put("date", cooperation.getDateCreated().toString());
                data.put("user", currentUser.getFullName());
                message.setData(data);

                firebaseMessagingService.sendNotificationByToken(message);
            }
        }
    }

    private String estimateYield(Double value) {
        if (value >= 1000) {
            return value / 1000 + " tấn";
        } else if (value >= 100) {
            return value / 100 + " tạ";
        } else if (value >= 10) {
            return value / 10 + " yến";
        } else return value + " kg";
    }
}
