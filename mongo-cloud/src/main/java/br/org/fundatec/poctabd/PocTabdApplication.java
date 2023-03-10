package br.org.fundatec.poctabd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class PocTabdApplication {

	public static void main(String[] args) {
		SpringApplication.run(PocTabdApplication.class, args);
	}

}
