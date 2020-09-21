package com.billing.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

/**
 * @author Hafiz
 */
@EnableRetry
@SpringBootApplication
public class BillingServiceAppApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillingServiceAppApiApplication.class, args);
	}

}
