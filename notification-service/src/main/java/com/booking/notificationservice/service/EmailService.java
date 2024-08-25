package com.booking.notificationservice.service;

// sửa trường abc của createdAt
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.booking.notificationservice.dto.request.EmailRequest;
import com.booking.notificationservice.dto.request.SendEmailRequest;
import com.booking.notificationservice.dto.request.Sender;
import com.booking.notificationservice.dto.response.EmailResponse;
import com.booking.notificationservice.exception.AppException;
import com.booking.notificationservice.exception.ErrorCode;
import com.booking.notificationservice.repository.httpclient.EmailClient;
import com.booking.notificationservice.utils.EmailTemplateUtil;

import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    EmailClient emailClient;
    EmailTemplateUtil emailTemplateUtil;

    @Value("${brevo.apiKey}")
    @NonFinal
    String apiKey;

    @Value("${web.url}")
    @NonFinal
    String webUrl;

    private EmailResponse sendEmail(EmailRequest emailRequest) {
        try {
            return emailClient.sendEmail(apiKey, emailRequest);
        } catch (FeignException e) {
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }

    private EmailRequest builtEmailRequest(SendEmailRequest request, String htmlContent){
        return  EmailRequest.builder()
                            .sender(Sender.builder()
                                    .name("Booking.com")
                                    .email("numberzero0909@gmail.com")
                                    .build())
                            .to(List.of(request.getTo()))
                            .subject((String) request.getParams().get("subject"))
                            .htmlContent(htmlContent)
                            .build();
    }

    public EmailResponse sendWelcomeEmail(SendEmailRequest request) {
        Map<String, Object> variables = emailTemplateUtil
                .createVariables("webUrl", webUrl, "email", request.getTo().getEmail(), "createdAt",
                        (String) request.getParams().get("createdAt"));

        var htmlContent = emailTemplateUtil.generateContent(request.getTemplateCode(), variables);

        EmailRequest emailRequest = builtEmailRequest(request, htmlContent);
        return sendEmail(emailRequest);
    }

    public EmailResponse sendResetPasswordEmail(SendEmailRequest request) {
        Map<String, Object> variables = emailTemplateUtil
                .createVariables("webUrl", webUrl, "email", request.getTo().getEmail(), 
                        "resetLink", (String) request.getParams().get("resetLink"), 
                        "imageUrl", (String) request.getParams().get("imageUrl"));

        var htmlContent = emailTemplateUtil.generateContent(request.getTemplateCode(), variables);

        EmailRequest emailRequest = builtEmailRequest(request, htmlContent);

        return sendEmail(emailRequest);
    }

    public EmailResponse sendLoginMagicLink(SendEmailRequest request){
        Map<String, Object> variables = emailTemplateUtil
              .createVariables("webUrl", webUrl, "loginMagicLink",
                                 request.getParams().get("loginMagicLink"));

        var htmlContent = emailTemplateUtil.generateContent(request.getTemplateCode(), variables);

        EmailRequest emailRequest = builtEmailRequest(request, htmlContent);

        return sendEmail(emailRequest);
    }
    
    public EmailResponse sendListingRentalApprovalEmail(SendEmailRequest request){
        Map<String, Object> variables = emailTemplateUtil
                                    .createVariables("webUrl", webUrl);// thiếu thông tin người đặt phòng

        var htmlContent = emailTemplateUtil.generateContent(request.getTemplateCode(), variables);

        EmailRequest emailRequest = builtEmailRequest(request, htmlContent);

         return sendEmail(emailRequest);
    }
}