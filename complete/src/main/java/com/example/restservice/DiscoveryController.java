package com.example.restservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class DiscoveryController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DiscoveryController.class);
	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/discovery")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) throws UnknownHostException {
		LOGGER.info("Discovery called for {} at counter {} on {}", name, counter.incrementAndGet(), InetAddress.getLocalHost().getHostName());
		return new Greeting(counter.longValue(), String.format(template, name));
	}
}
