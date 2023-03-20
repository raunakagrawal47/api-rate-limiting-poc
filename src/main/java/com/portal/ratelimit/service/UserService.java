package com.portal.ratelimit.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.portal.ratelimit.exception.UserNotFoundException;
import com.portal.ratelimit.model.User;

@Service
public class UserService {

	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	private static HashMap<String, User> rateLimitingMap = new HashMap(){{
		put("bananarepublic", new User(1, "bananarepublic", 10));
		put("rapha", new User(2, "rapha", 20));
		put("carhatt", new User(3, "carhatt", 15));
		put("lenskart", new User(4, "carhatt", 30));
	}};
	public User getUser(String  userKey) {
		User userOp = rateLimitingMap.get(userKey);
		if (userOp != null) {
			return userOp;
		} else {
			throw new UserNotFoundException("user not found");
		}
	}
}
