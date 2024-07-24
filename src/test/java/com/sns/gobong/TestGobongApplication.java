package com.sns.gobong;

import org.springframework.boot.SpringApplication;

public class TestGobongApplication {

	public static void main(String[] args) {
		SpringApplication.from(GobongApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
