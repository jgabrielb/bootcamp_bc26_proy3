package com.nttdata.msgatewayclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MsGatewayClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsGatewayClientApplication.class, args);
	}

}
