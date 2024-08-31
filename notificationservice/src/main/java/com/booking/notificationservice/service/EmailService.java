package com.booking.notificationservice.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.booking.notificationservice.dto.request.EmailRequest;
import com.booking.notificationservice.dto.request.Receiver;
import com.booking.notificationservice.dto.request.SendEmailRequest;
import com.booking.notificationservice.dto.request.Sender;
import com.booking.notificationservice.dto.response.EmailResponse;
import com.booking.notificationservice.entity.AppNotification;
import com.booking.notificationservice.entity.NotificationSetting;
import com.booking.notificationservice.exception.AppException;
import com.booking.notificationservice.exception.ErrorCode;
import com.booking.notificationservice.repository.AppNotificationRepository;
import com.booking.notificationservice.repository.NotificationSettingRepository;
import com.booking.notificationservice.repository.httpclient.EmailClient;
import com.booking.notificationservice.utils.DateTimeFormatUtil;
import com.booking.notificationservice.utils.EmailTemplateUtil;

import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
        EmailClient emailClient;
        EmailTemplateUtil emailTemplateUtil;
        AppNotificationRepository appNotificationRepository;
        NotificationSettingRepository notificationSettingRepository;

        DateTimeFormatUtil dateTimeFormatUtil;

        @Value("${app.brevo.apiKey}")
        @NonFinal
        String apiKey;

        @Value("${app.frontend.url}")
        @NonFinal
        String frontendUrl;

        public EmailResponse sendWelcomeEmail(SendEmailRequest request) {
                AppNotification appNotification = AppNotification.builder()
                                .content(
                                                "Chào mừng bạn đến với Booking.com. Kiếm thêm thu nhập bằng cách đăng ký chỗ nghỉ trên Booking.com ")
                                .urlLabel("Tìm hiểu thêm")
                                .slug("#")
                                .userId((String) request.getParams().get("userId"))
                                .icon("tags")
                                .build();

                appNotificationRepository.save(appNotification);

                String formattedCreatedAt =  "";
                //dateTimeFormatUtil.format((Instant)request.getParams().get("createdAt"));
                
                Map<String, Object> variables = emailTemplateUtil
                                .createVariables("frontendUrl", frontendUrl,
                                                "email", request.getTo().getEmail(),
                                                "createdAt", formattedCreatedAt);

                var htmlContent = emailTemplateUtil.generateContent(request.getTemplateCode(), variables);

                EmailRequest emailRequest = builtEmailRequest(request, htmlContent);
                return sendEmail(emailRequest);
        }

        public EmailResponse sendResetPasswordEmail(SendEmailRequest request) {
                Map<String, Object> variables = emailTemplateUtil
                                .createVariables("frontendUrl", frontendUrl,
                                                "email", request.getTo().getEmail(),
                                                "resetLink", (String) request.getParams().get("resetLink"),
                                                "imageUrl", (String) request.getParams().get("imageUrl"));

                var htmlContent = emailTemplateUtil.generateContent(request.getTemplateCode(), variables);

                EmailRequest emailRequest = builtEmailRequest(request, htmlContent);

                return sendEmail(emailRequest);
        }

        public EmailResponse sendLoginMagicLink(SendEmailRequest request) {
                Map<String, Object> variables = emailTemplateUtil
                                .createVariables("frontendUrl", frontendUrl, "loginMagicLink",
                                                request.getParams().get("loginMagicLink"));

                var htmlContent = emailTemplateUtil.generateContent(request.getTemplateCode(), variables);

                EmailRequest emailRequest = builtEmailRequest(request, htmlContent);

                return sendEmail(emailRequest);
        }

        public EmailResponse sendListingRentalApprovalEmail(SendEmailRequest request) {
                AppNotification appNotification = AppNotification.builder()
                                .content(
                                                "Bạn có một yêu cầu đặt phòng cần chờ phê duyệt")
                                .urlLabel("Phê duyệt ngay")
                                .reservationId("")
                                .slug("#")
                                .userId("#")
                                .build();

                appNotificationRepository.save(appNotification);

                String userId = "a";
                Optional<NotificationSetting> settingOptional = notificationSettingRepository.findFirstByUserId(userId);
                if (settingOptional.isPresent()) {
                        NotificationSetting setting = settingOptional.get();
                        if (!setting.getReceiveBookingApprovalEmail()) {
                                return null;
                        }
                }

                Map<String, Object> variables = emailTemplateUtil
                                .createVariables("frontendUrl", frontendUrl);// thiếu thông tin người đặt phòng

                var htmlContent = emailTemplateUtil.generateContent(request.getTemplateCode(), variables);

                EmailRequest emailRequest = builtEmailRequest(request, htmlContent);

                return sendEmail(emailRequest);
        }

        public EmailResponse sendListingConfirmationEmail(SendEmailRequest request) {
                AppNotification appNotification = AppNotification.builder()
                                .content(
                                                "Yêu cầu đặt phòng của bạn đã được phê duyệt. Vui lòng thanh toán nếu bạn chọn phương thức đặt cọc")
                                .reservationId("")
                                .urlLabel("Xem ngay")
                                .slug("#")
                                .userId("#")
                                .build();
                appNotificationRepository.save(appNotification);

                String userId = "a";
                Optional<NotificationSetting> settingOptional = notificationSettingRepository.findFirstByUserId(userId);
                if (settingOptional.isPresent()) {
                        NotificationSetting setting = settingOptional.get();
                        if (!setting.getReceiveBookingConfirmedEmail()) {
                                return null;
                        }
                }

                Map<String, Object> variables = emailTemplateUtil
                                .createVariables("frontendUrl", frontendUrl);

                var htmlContent = emailTemplateUtil.generateContent(request.getTemplateCode(), variables);

                EmailRequest emailRequest = builtEmailRequest(request, htmlContent);

                return sendEmail(emailRequest);
        }

        public EmailResponse sendListingDenyEmail(SendEmailRequest request) {
                AppNotification appNotification = AppNotification.builder()
                                .content(
                                                "Yêu cầu đặt phòng của bạn đã bị từ chối")
                                .urlLabel("Xem ngay")
                                .slug("#")
                                .userId("#")
                                .build();
                appNotificationRepository.save(appNotification);

                String userId = "a";
                Optional<NotificationSetting> settingOptional = notificationSettingRepository.findFirstByUserId(userId);
                if (settingOptional.isPresent()) {
                        NotificationSetting setting = settingOptional.get();
                        if (!setting.getReceiveBookingDenyEmail()) {
                                return null;
                        }
                }

                Map<String, Object> variables = emailTemplateUtil
                                .createVariables("frontendUrl", frontendUrl);

                var htmlContent = emailTemplateUtil.generateContent(request.getTemplateCode(), variables);

                EmailRequest emailRequest = builtEmailRequest(request, htmlContent);

                return sendEmail(emailRequest);
        }

        private EmailResponse sendEmail(EmailRequest emailRequest) {
                try {
                        return emailClient.sendEmail(apiKey, emailRequest);
                } catch (FeignException e) {
                        throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
                }
        }

        private EmailRequest builtEmailRequest(SendEmailRequest request, String htmlContent) {
                return EmailRequest.builder()
                                .sender(Sender.builder()
                                                .name("Booking.com")
                                                .email("numberzero0909@gmail.com")
                                                .build())
                                .to(List.of(request.getTo()))
                                .subject((String) request.getParams().get("subject"))
                                .htmlContent(htmlContent)
                                .build();
        }

        public EmailResponse sendEmailTest(SendEmailRequest request) {
                log.info(apiKey);
                EmailRequest emailRequest = EmailRequest.builder()
                                .sender(Sender.builder()
                                                .name("Test email")
                                                .email("numberzero0909@gmail.com")
                                                .build())
                                .to(List.of(Receiver.builder().name("hello").email("testaaodsodf@yopmail.com").build()))
                                .subject("oke")
                                .htmlContent("zyx")
                                .build();
                try {
                        return emailClient.sendEmail(apiKey, emailRequest);
                } catch (FeignException e) {
                        throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
                }
        }
}