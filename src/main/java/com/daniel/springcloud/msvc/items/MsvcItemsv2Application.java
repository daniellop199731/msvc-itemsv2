package com.daniel.springcloud.msvc.items;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsvcItemsv2Application {

	public static void main(String[] args) {
		SpringApplication.run(MsvcItemsv2Application.class, args);
	}

}
