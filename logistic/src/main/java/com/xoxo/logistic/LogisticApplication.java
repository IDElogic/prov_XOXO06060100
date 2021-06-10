package com.xoxo.logistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.xoxo.logistic.config.TransportConfigProperties;
import com.xoxo.logistic.service.InitDBService;

@SpringBootApplication
public class LogisticApplication implements CommandLineRunner {

	
	@Autowired
	InitDBService initDBService;
	
	@Autowired
	TransportConfigProperties config;
	
	public static void main(String[] args) {
		SpringApplication.run(LogisticApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (!config.isTest()) {
			initDBService.initDb();	
		}
	}
}




