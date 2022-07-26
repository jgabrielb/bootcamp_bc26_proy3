package com.nttdata.msdebitcard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MsDebitCardApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsDebitCardApplication.class, args);
	}

}
