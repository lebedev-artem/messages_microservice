package ru.skillbox.socialnetwork.messages;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MessagesMicroServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessagesMicroServiceApplication.class, args);
	}

}
