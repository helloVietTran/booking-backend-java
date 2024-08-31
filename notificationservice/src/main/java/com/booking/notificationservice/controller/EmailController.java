package com.booking.notificationservice.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.booking.event.dto.NotificationEvent;
import com.booking.notificationservice.dto.ApiResponse;
import com.booking.notificationservice.dto.request.Receiver;
import com.booking.notificationservice.dto.request.SendEmailRequest;
import com.booking.notificationservice.dto.response.EmailResponse;
import com.booking.notificationservice.service.EmailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailController {
    EmailService emailService;
    @KafkaListener(topics = "welcomEmail-notifcation")
    public void listenNotificationRegistration(NotificationEvent message){
        emailService.sendWelcomeEmail(builtEmailRequest(message));
    }

    @KafkaListener(topics= "resetPasswordLink-notifcation")
    public void listenNotificationResetPasswordLink(NotificationEvent message){
        emailService.sendResetPasswordEmail(builtEmailRequest(message));
    }

    @KafkaListener(topics= "magicLoginLink-notifcation-output")
    public void listenNotificationLoginMagicLink(NotificationEvent message){
        emailService.sendLoginMagicLink(builtEmailRequest(message));
    }

    @KafkaListener(topics = "bookingApproval-notifcation")
    public void listenNotificationListingRentalApproval(NotificationEvent message){
        emailService.sendListingRentalApprovalEmail(builtEmailRequest(message));
    }
    
    @KafkaListener(topics = "bookingConfirmation-notifcation")
    public void listenNotificationListingConfirmation(NotificationEvent message){
        emailService.sendListingConfirmationEmail(builtEmailRequest(message));
    }
    
    @KafkaListener(topics = "bookingDeny-notifcation")
    public void listenNotificationListingDeny(NotificationEvent message){
        emailService.sendListingDenyEmail(builtEmailRequest(message));
    }
    
    private SendEmailRequest builtEmailRequest(NotificationEvent message){
        return SendEmailRequest
                .builder()
                .to(Receiver.builder().email(message.getReceiver()).build())
                .params(message.getParams())
                .templateCode(message.getTemplateCode())
                .build();
    }

    @PostMapping("/email/send")
    ApiResponse<EmailResponse> sendEmail(@RequestBody SendEmailRequest request){
        
        return ApiResponse.<EmailResponse>builder()
                .result(emailService.sendEmailTest(request))
                .build();
    }
}
