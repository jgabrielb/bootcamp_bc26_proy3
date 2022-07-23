package com.nttdata.mssignatories;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MsSignatoriesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsSignatoriesApplication.class, args);
	}

}
