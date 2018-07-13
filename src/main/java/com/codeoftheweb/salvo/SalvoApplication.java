package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {

		SpringApplication.run(SalvoApplication.class, args);

	}

    @Bean
    public CommandLineRunner initData(PlayerRepository repository) {
        return (args) -> {
            // save a couple of customers
            repository.save(new Player("Jack", "Bauer", "beast"));
            repository.save(new Player("Chloe", "O'Brian", "scott"));
            repository.save(new Player("Kim", "Bauer", "zuka"));
            repository.save(new Player("David", "Palmer","blyet"));
            repository.save(new Player("Michelle", "Dessler", "badabing"));
        };
    }


}
