package com.nttdata.mscustomers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MsCustomersApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsCustomersApplication.class, args);
    }

}
