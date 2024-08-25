package com.booking.paymentservice.mapper;

import org.mapstruct.Mapper;

import com.booking.paymentservice.dto.request.WalletCreationRequest;
import com.booking.paymentservice.dto.response.WalletResponse;
import com.booking.paymentservice.entity.CoinWallet;

@Mapper(componentModel = "spring")
public interface CoinWalletMapper {
    CoinWallet toCoinWallet(WalletCreationRequest request);
    WalletResponse toWalletResponse(CoinWallet wallet);
}
