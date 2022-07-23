package com.nttdata.mswithdrawals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MsWithdrawalsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsWithdrawalsApplication.class, args);
	}

}
