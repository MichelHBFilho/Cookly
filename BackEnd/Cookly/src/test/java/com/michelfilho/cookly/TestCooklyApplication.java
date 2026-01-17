package com.michelfilho.cookly;

import org.springframework.boot.SpringApplication;

public class TestCooklyApplication {

	public static void main(String[] args) {
		SpringApplication.from(CooklyApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
