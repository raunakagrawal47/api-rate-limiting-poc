package com.portal.ratelimit.controller;

import com.portal.ratelimit.service.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

	@Autowired
	RateLimiter rateLimiter;

	@GetMapping("/v1/user")
	public String getUser() {
		return "Hello Secure User";
	}
	
	@GetMapping("/v2/user")
	public String getUserNotsecure() {
		return "Hello Not Secure User";
	}
	@GetMapping("/update/{retailer}/{newTokenLimit}")
	public void updateTokenConfiguraton(@PathVariable String retailer, @PathVariable int newTokenLimit) {
		rateLimiter.updateTokenConfiguration(retailer, newTokenLimit);
		System.out.println("Updated success");
	}
}
