package com.booking.notificationservice.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.booking.event.dto.NotificationEvent;
import com.booking.notificationservice.dto.request.Receiver;
import com.booking.notificationservice.dto.request.SendEmailRequest;
import com.booking.notificationservice.service.EmailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    EmailService emailService;
    
    @KafkaListener(topics = "notification-registration")// thông báo consumer
    public void listenNotificationRegistration(NotificationEvent message){// nhận được biến ở đây
        emailService.sendWelcomeEmail(SendEmailRequest
                                .builder()
                                .to(Receiver.builder().email(message.getReceiver()).build())
                                .templateCode(message.getTemplateCode())
                                .params(message.getParams())
                                .build());
    }

    @KafkaListener(topics= "notification-reset-password-link")
    public void listenNotificationResetPasswordLink(NotificationEvent message){
        
        emailService.sendResetPasswordEmail(SendEmailRequest
                                .builder()
                                .to(Receiver.builder().email(message.getReceiver()).build())
                                .params(message.getParams())
                                .templateCode(message.getTemplateCode())
                                .build());
    }

    @KafkaListener(topics= "notification-login-magic-link")
    public void listenNotificationLoginMagicLink(NotificationEvent message){
    
        emailService.sendLoginMagicLink(SendEmailRequest
                                .builder()
                                .to(Receiver.builder().email(message.getReceiver()).build())
                                .params(message.getParams())
                                .templateCode(message.getTemplateCode())
                                .build());
    }

    @KafkaListener(topics = "notification-listing-rental-approval")
    public void listenNotificationListingRentalApproval(NotificationEvent message){
    
        emailService.sendListingRentalApprovalEmail(SendEmailRequest
                                .builder()
                                .to(Receiver.builder().email(message.getReceiver()).build())
                                .params(message.getParams())
                                .templateCode(message.getTemplateCode())
                                .build());
    }

}
