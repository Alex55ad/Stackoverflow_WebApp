package com.utcn.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;

@SpringBootApplication
@EnableJpaRepositories
public class 	DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		openBrowser("http://localhost:8080/");
	}

	private static void openBrowser(String url) {
		String os = System.getProperty("os.name").toLowerCase();
		Runtime rt = Runtime.getRuntime();

		try {
			if (os.contains("win")) {
				rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
			} else if (os.contains("mac")) {
				rt.exec("open " + url);
			} else if (os.contains("nix") || os.contains("nux")) {
				rt.exec("xdg-open " + url);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
