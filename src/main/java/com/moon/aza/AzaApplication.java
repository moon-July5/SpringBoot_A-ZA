package com.moon.aza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableJpaAuditing
@SpringBootApplication
public class AzaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AzaApplication.class, args);
	}

}
