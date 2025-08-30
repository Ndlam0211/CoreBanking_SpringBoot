package com.lamnd.corebanking;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Core Banking API",
                version = "v1.0",
                description = "Restful APIs for Core Banking Operations",
                contact= @Contact(
                        name = "Nguyen Dinh Lam",
                        email = "nguyendinhlam0211@gmail.com",
                        url = "https://github.com/Ndlam0211/CoreBanking_SpringBoot"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://github.com/Ndlam0211/CoreBanking_SpringBoot"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Core Banking API Documentation",
                url = "https://github.com/Ndlam0211/CoreBanking_SpringBoot"
        )
)
public class CorebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CorebankingApplication.class, args);
	}

}
