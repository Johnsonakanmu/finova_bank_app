package com.finova;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

// for Swagger implementation
@OpenAPIDefinition(
		info = @Info(
				title = "Spring boot Rest API Documentation",
				description = "Spring boot Rest API Documentation",
				version = "v1.0",
				contact = @Contact(
						name = "Johnson",
						email = "johnsonakanmu@gmail.com",
						url = "https://www.johnsoncodelove.net"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://www.johnsoncodelove.net/license"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Spring Boot Finova Banking Documentation",
				url = "https://www.johnsoncodelove.net/user_management.html"
		)
)

public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
