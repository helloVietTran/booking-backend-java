package com.booking.listingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ListingserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ListingserviceApplication.class, args);
	}

}
