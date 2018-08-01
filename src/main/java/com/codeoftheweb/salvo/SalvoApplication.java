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
                                      GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository,
                                      SalvoRepository salvoRepository) {
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
            GamePlayer game1Chloe = new GamePlayer(game1, chloe);
            GamePlayer game2kim = new GamePlayer(game2, kim);
            GamePlayer game2david = new GamePlayer(game2, david);

            gamePlayerRepository.save(game1jack);
            gamePlayerRepository.save(game1Chloe);
            gamePlayerRepository.save(game2kim);
            gamePlayerRepository.save(game2david);



            List<String> location1 = Arrays.asList("H1","H2","H3");
            List<String> location2 = Arrays.asList("B1","B2");
            List<String> location3 = Arrays.asList("F1","F2","F3");
            List<String> location4 = Arrays.asList("E1","E2","E3");

            List<String> location5 = Arrays.asList("A1","B1","C1");
            List<String> location6 = Arrays.asList("B3","C3");
            List<String> location7 = Arrays.asList("F5","G5","H5");
            List<String> location8 = Arrays.asList("H10","I10");

            Ship ship1 = new Ship("cruiser", location1, game1jack);
            Ship ship3 = new Ship("caller", location2, game1jack);
            Ship ship2 = new Ship("bawler", location3, game1Chloe);
            Ship ship4 = new Ship("bruiser", location4, game1Chloe);

            Ship ship5 = new Ship("mooner", location5, game2kim);
            Ship ship6 = new Ship("morrow", location6, game2kim);
            Ship ship7 = new Ship("sooner", location7, game2david);
            Ship ship8 = new Ship("sorrow", location8, game2david);

            shipRepository.save(ship1);
            shipRepository.save(ship2);
            shipRepository.save(ship3);
            shipRepository.save(ship4);

            shipRepository.save(ship5);
            shipRepository.save(ship6);
            shipRepository.save(ship7);
            shipRepository.save(ship8);

            Salvo salvo1 = new Salvo(1,location1,game1jack);
            Salvo salvo2 = new Salvo(2,location2,game1jack);
            Salvo salvo3 = new Salvo(3,location3,game1jack);
            Salvo salvo4 = new Salvo(4,location4,game1jack);

            Salvo salvo5 = new Salvo(1,location5,game1Chloe);
            Salvo salvo6 = new Salvo(2,location6,game1Chloe);
            Salvo salvo7 = new Salvo(3,location7,game1Chloe);
            Salvo salvo8 = new Salvo(4,location8,game1Chloe);
//            Salvo salvo2 = new Salvo(2,location6,game1Chloe);
//            Salvo salvo3 = new Salvo(1,location7,game2kim);
//            Salvo salvo4 = new Salvo(2,location8,game2david);
            salvoRepository.save(salvo1);
            salvoRepository.save(salvo2);
            salvoRepository.save(salvo3);
            salvoRepository.save(salvo4);

            salvoRepository.save(salvo5);
            salvoRepository.save(salvo6);
            salvoRepository.save(salvo7);
            salvoRepository.save(salvo8);

//




       };
    }




}
