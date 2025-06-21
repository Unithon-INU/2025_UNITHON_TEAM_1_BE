package com.example.unithon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UnithonApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnithonApplication.class, args);
	}

}
