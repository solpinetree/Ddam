package com.ddam.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

@EnableJpaAuditing
@SpringBootApplication
public class DdamApplication {

	public static void main(String[] args) {
		SpringApplication.run(DdamApplication.class, args);
	}

	@Bean
	public Java8TimeDialect java8TimeDialect() {
		return new Java8TimeDialect();
	}
}
