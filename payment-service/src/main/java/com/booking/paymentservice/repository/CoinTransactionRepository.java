package com.booking.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.paymentservice.entity.CoinTransaction;

public interface CoinTransactionRepository extends JpaRepository<CoinTransaction, String>{
    
}
