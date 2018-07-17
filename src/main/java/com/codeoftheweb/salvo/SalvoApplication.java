package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Calendar;
import java.util.Date;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {

		SpringApplication.run(SalvoApplication.class, args);

	}

    @Bean
    public CommandLineRunner initDAta(PlayerRepository playerRepository, GameRepository gameRepository,
                                      GamePlayerRepository gamePlayerRepository) {
        return (args) -> {
             //save a couple of customers
            Player jack = new Player( "jack@bauer.com");
            playerRepository.save(jack);
            playerRepository.save(new Player("chloe@obrian.com"));
            playerRepository.save(new Player( "kim@bauer.com"));
            playerRepository.save(new Player("david@palmer.com"));
            playerRepository.save(new Player("michelle@dessler.com"));
//
//
            Game game1 = new Game();
            gameRepository.save(game1);
            Date game1Date = game1.getCreationDate();
            Date game2Date = Date.from(game1Date.toInstant().plusSeconds(3600));
            Date game3Date = Date.from(game1Date.toInstant().plusSeconds(7200));
            Game game2 = new Game();
            Game game3 = new Game();
            game2.setCreationDate(game2Date);
            game3.setCreationDate(game3Date);
            gameRepository.save(game2);
            gameRepository.save(game3);

            gamePlayerRepository.save(new GamePlayer(game1, jack));


       };
    }




}
