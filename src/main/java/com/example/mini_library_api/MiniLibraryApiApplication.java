package com.example.mini_library_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class MiniLibraryApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(MiniLibraryApiApplication.class, args);
	}

}
