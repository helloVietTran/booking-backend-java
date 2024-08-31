package com.booking.fileservice.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.booking.fileservice.config.AuthenticationRequestInterceptor;
import com.booking.fileservice.dto.request.UpdateListingImageRequest;

@FeignClient(name="listing-service", url ="${app.services.listing}", configuration = {AuthenticationRequestInterceptor.class})
public interface ListingClient {
    @PatchMapping(value = "/my/listing/image", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object updateListingImage(@RequestBody UpdateListingImageRequest request);
}
