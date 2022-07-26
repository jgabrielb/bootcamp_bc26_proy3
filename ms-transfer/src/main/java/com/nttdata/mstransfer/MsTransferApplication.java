package com.nttdata.mstransfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MsTransferApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsTransferApplication.class, args);
	}

}
