package com.booking.bookingservice.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.booking.bookingservice.config.AuthenticationRequestInterceptor;
import com.booking.bookingservice.dto.request.UpdateListingStatusRequest;
import com.booking.bookingservice.dto.response.ListingResponse;

@FeignClient(name = "listing-service", url = "${app.services.listing}", configuration = AuthenticationRequestInterceptor.class)
public interface ListingClient {
    @GetMapping(value = "/{listingId}")
    public ListingResponse getListing(@PathVariable String listingId);

    @PutMapping(value = "/reservation")
    public Object setListingStatusToBooked(@RequestBody UpdateListingStatusRequest request);
}
