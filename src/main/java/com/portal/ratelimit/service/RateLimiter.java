package com.portal.ratelimit.service;

import java.time.Duration;
import java.util.function.Supplier;

import io.github.bucket4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.portal.ratelimit.model.User;
import io.github.bucket4j.distributed.proxy.ProxyManager;

@Service
public class RateLimiter {

	@Autowired
	UserService userService;
	
	@Autowired
	ProxyManager<String> proxyManager;
	
	public Bucket resolveBucket(String key) {
        Supplier<BucketConfiguration> configSupplier = getConfigSupplierForUser(key);
        
        return proxyManager.builder().build(key, configSupplier);
    }

    private Supplier<BucketConfiguration> getConfigSupplierForUser(String userId) {
        User user = userService.getUser(userId);
        
        Refill refill = Refill.intervally(user.getLimit(), Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(user.getLimit(), refill);
        return () -> (BucketConfiguration.builder()
                .addLimit(limit)
                .build());
    }

    public void updateTokenConfiguration(String retailer, int newLimit){
        Bucket bucket = resolveBucket(retailer);
        Refill refill = Refill.intervally(newLimit, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(newLimit, refill);

        BucketConfiguration newConfiguration = BucketConfiguration.builder()
                .addLimit(limit)
                .build();

        bucket.replaceConfiguration(newConfiguration, TokensInheritanceStrategy.RESET);
    }
	
}
