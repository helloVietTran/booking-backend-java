package com.booking.paymentservice.service;

import org.springframework.stereotype.Service;

import com.booking.paymentservice.dto.request.WalletCreationRequest;
import com.booking.paymentservice.dto.response.WalletResponse;
import com.booking.paymentservice.entity.CoinWallet;
import com.booking.paymentservice.mapper.CoinWalletMapper;
import com.booking.paymentservice.repository.CoinWalletRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {
    CoinWalletMapper coinWalletMapper;
    CoinWalletRepository coinWalletRepository;

    public WalletResponse createWallet(WalletCreationRequest request){
        CoinWallet coinWallet = CoinWallet.builder()
                            .userId(request.getUserId())
                            .build();
        
        coinWalletRepository.save(coinWallet);

        return coinWalletMapper.toWalletResponse(coinWallet);
    }
}
