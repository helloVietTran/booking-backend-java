package com.booking.paymentservice.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.booking.paymentservice.dto.ApiResponse;
import com.booking.paymentservice.dto.request.WalletCreationRequest;
import com.booking.paymentservice.dto.response.WalletResponse;
import com.booking.paymentservice.service.PaymentService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    PaymentService paymentService;

    @PostMapping("/coin-wallet")
    
    ApiResponse<WalletResponse> createWallet(@RequestBody WalletCreationRequest request){
        return ApiResponse.<WalletResponse>builder()
                        .result(paymentService.createWallet(request))
                        .build();
    }

    
}
