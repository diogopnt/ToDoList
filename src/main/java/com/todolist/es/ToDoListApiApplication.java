package com.todolist.es;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ToDoListApiApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().load();
		System.setProperty("EXTERNAL_AUTH_TOKEN_URL", dotenv.get("EXTERNAL_AUTH_TOKEN_URL"));
		System.setProperty("EXTERNAL_AUTH_CLIENT_CREDENTIALS", dotenv.get("EXTERNAL_AUTH_CLIENT_CREDENTIALS"));
		System.setProperty("COGNITO_JWKS_URL", dotenv.get("COGNITO_JWKS_URL"));

		SpringApplication.run(ToDoListApiApplication.class, args);
	}
}
