package com.booking.listingservice.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.booking.listingservice.dto.response.ReservationResponse;
import com.booking.listingservice.config.AuthenticationRequestInterceptor;

@FeignClient(name="booking-service", url ="${app.services.booking}", configuration = {AuthenticationRequestInterceptor.class})
public interface ReservationClient {
    @GetMapping(value = "/{listingId}/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReservationResponse getReservation(@PathVariable String listingId, @PathVariable String userId);

}

