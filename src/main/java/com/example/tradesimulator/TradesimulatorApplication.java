package com.example.tradesimulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TradesimulatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradesimulatorApplication.class, args);
	}

}
