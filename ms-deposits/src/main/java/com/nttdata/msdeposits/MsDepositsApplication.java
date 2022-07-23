package com.nttdata.msdeposits;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MsDepositsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsDepositsApplication.class, args);
	}

}
