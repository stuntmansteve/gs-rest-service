package com.example.restservice;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.amazonaws.services.servicediscovery.AWSServiceDiscovery;
import com.amazonaws.services.servicediscovery.AWSServiceDiscoveryClientBuilder;
import com.amazonaws.services.servicediscovery.model.*;
import javafx.scene.shape.LineToBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class GreetingController {

	private static final Logger LOGGER = LoggerFactory.getLogger(GreetingController.class);
	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@Value("${spring.datasource.username}")
	private String oracleUsername;

	private RestTemplate restTemplate;

	public GreetingController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) throws UnknownHostException {
		LOGGER.info("Greeting called for {} at counter {} on {}", name, counter.incrementAndGet(), InetAddress.getLocalHost().getHostName());

		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<>(headers);
		if ("World".equals(name)) {
			LOGGER.info("Going to call discovery ...");

			AWSServiceDiscovery awsServiceDiscovery = AWSServiceDiscoveryClientBuilder.defaultClient();
			ListServicesRequest listServicesRequest = new ListServicesRequest();
			List<ServiceFilter> serviceFilters = new ArrayList<>();
			ServiceFilter serviceFilter = new ServiceFilter();
			serviceFilter.withName("NAMESPACE_ID");
			serviceFilter.withValues("SUPER_COOL_VALUE_HERE");
//			serviceFilters.add(serviceFilter);
			listServicesRequest.setFilters(serviceFilters);
			ListServicesResult result = awsServiceDiscovery.listServices(listServicesRequest);
			ListInstancesRequest listInstancesRequest = new ListInstancesRequest();
			listInstancesRequest.setServiceId(result.getServices().get(1).getId());
			ListInstancesResult listInstancesResult = awsServiceDiscovery.listInstances(listInstancesRequest);
			String s = listInstancesResult.getInstances().get(0).getAttributes().get("AWS_INSTANCE_IPV4");
			LOGGER.info("Checking name {}", "http://" + s + ":8080/discovery");
			restTemplate.exchange("http://" + s + ":8080/discovery", HttpMethod.GET, entity, Object.class);
		}

		return new Greeting(counter.longValue(), String.format(template, name));
	}
}
