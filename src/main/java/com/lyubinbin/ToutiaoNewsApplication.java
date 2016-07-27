package com.lyubinbin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class ToutiaoNewsApplication extends SpringBootServletInitializer{
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ToutiaoNewsApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(ToutiaoNewsApplication.class, args);
	}
}
