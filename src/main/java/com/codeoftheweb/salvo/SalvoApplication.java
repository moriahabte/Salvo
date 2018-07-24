package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.*;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {

		SpringApplication.run(SalvoApplication.class, args);

	}

    @Bean
    public CommandLineRunner initDAta(PlayerRepository playerRepository, GameRepository gameRepository,
                                      GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository) {
        return (args) -> {
             //save a couple of customers
            Player jack = new Player( "jack@bauer.com");
            Player chloe = new Player( "chloe@obrian.com");
            Player kim = new Player( "kim@bauer.com");
            Player david = new Player( "david@palmer.com");
            Player michelle = new Player( "michelle@dessler.com");

            playerRepository.save(jack);
            playerRepository.save(chloe);
            playerRepository.save(kim);
            playerRepository.save(david);
            playerRepository.save(michelle);
//
//
            Game game1 = new Game();
            Date game1Date = game1.getCreationDate();
            Date game2Date = Date.from(game1Date.toInstant().plusSeconds(3600));
            Date game3Date = Date.from(game1Date.toInstant().plusSeconds(7200));
            Game game2 = new Game();
            Game game3 = new Game();
            game2.setCreationDate(game2Date);
            game3.setCreationDate(game3Date);

            gameRepository.save(game1);
            gameRepository.save(game2);
            gameRepository.save(game3);




            GamePlayer game1jack = new GamePlayer(game1, jack);
            gamePlayerRepository.save(game1jack);
            gamePlayerRepository.save(new GamePlayer(game1, chloe));
            gamePlayerRepository.save(new GamePlayer(game2, kim));
            gamePlayerRepository.save(new GamePlayer(game2, david));
            gamePlayerRepository.save(new GamePlayer(game3, michelle));

            List<String> location1 = Arrays.asList("H1","H2","H3");

            Ship ship1 = new Ship("cruiser", location1, game1jack);
//            Ship ship2 = new Ship("bawler", new ArrayList<>(Arrays.asList("b1", "b2", "b3")), game1jack);

            shipRepository.save(ship1);
//            shipRepository.save(ship2);

//




       };
    }




}
