package com.booking.paymentservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.paymentservice.entity.CoinWallet;

public interface CoinWalletRepository extends JpaRepository<CoinWallet, String> {
    Optional<CoinWallet> findByUserId(String userId);
}
