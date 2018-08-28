package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@SpringBootApplication
public class SalvoApplication  {

	public static void main(String[] args) {

		SpringApplication.run(SalvoApplication.class, args);

	}

    @Bean
    public CommandLineRunner initDAta(PlayerRepository playerRepository, GameRepository gameRepository,
                                      GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository,
                                      SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
        return (args) -> {
             //save a couple of customers
            Player jack = new Player( "jack@bauer.com","24");
            Player chloe = new Player( "chloe@obrian.com", "42");
            Player kim = new Player( "kim@bauer.com", "kb");
            Player tony = new Player( "tony@almeida.com", "mole");

            playerRepository.save(jack);
            playerRepository.save(chloe);
            playerRepository.save(kim);
            playerRepository.save(tony);

            Game game1 = new Game();
            Date game1Date = game1.getCreationDate();
            Date game2Date = Date.from(game1Date.toInstant().plusSeconds(3600));
            Date game3Date = Date.from(game1Date.toInstant().plusSeconds(7200));
            Game game2 = new Game();
            Game game3 = new Game();
            game2.setCreationDate(game2Date);
            game3.setCreationDate(game3Date);
//
            gameRepository.save(game1);
            gameRepository.save(game2);
//            gameRepository.save(game3);
//
            GamePlayer game1jack = new GamePlayer(game1, jack);
            GamePlayer game1Chloe = new GamePlayer(game1, chloe);
            GamePlayer game2kim = new GamePlayer(game2, kim);
            GamePlayer game2david = new GamePlayer(game2, tony);
//
            gamePlayerRepository.save(game1jack);
            gamePlayerRepository.save(game1Chloe);
            gamePlayerRepository.save(game2kim);
            gamePlayerRepository.save(game2david);
//
//            List<String> location1 = Arrays.asList("H1","H2","H3");
//            List<String> location2 = Arrays.asList("B1","B2");
//            List<String> location3 = Arrays.asList("F1","F2","F3");
//            List<String> location4 = Arrays.asList("E1","E2","E3");
//
//            List<String> location5 = Arrays.asList("A1","B1","C1");
//            List<String> location6 = Arrays.asList("B3","C3");
//            List<String> location7 = Arrays.asList("F5","G5","H5");
//            List<String> location8 = Arrays.asList("H10","I10");
//
//            Ship ship1 = new Ship("Carrier", location1, game1jack);
//            Ship ship3 = new Ship("Battleship",location2, game1jack);
//            Ship ship2 = new Ship("Submarine", location3, game1jack);
//            Ship ship4 = new Ship("Destroyer", location4,game1jack);
//            Ship ship5 = new Ship("Patrol Boat", location5, game1jack);
//
////            Ship ship1 = new Ship("Carrier");
////            Ship ship3 = new Ship("Battleship");
////            Ship ship2 = new Ship("Submarine");
////            Ship ship4 = new Ship("Destroyer");
////            Ship ship5 = new Ship("Patrol Boat");
//
//
//            shipRepository.save(ship1);
//            shipRepository.save(ship2);
//            shipRepository.save(ship3);
//            shipRepository.save(ship4);
//            shipRepository.save(ship5);
////            ship1.setShipLocation(location1);
//
//
//            Salvo salvo1 = new Salvo(1,location1,game1jack);
//            Salvo salvo2 = new Salvo(2,location2,game1jack);
//            Salvo salvo3 = new Salvo(3,location3,game1jack);
//            Salvo salvo4 = new Salvo(4,location4,game1jack);
//
//            Salvo salvo5 = new Salvo(1,location5,game1Chloe);
//            Salvo salvo6 = new Salvo(2,location6,game1Chloe);
//            Salvo salvo7 = new Salvo(3,location7,game1Chloe);
//            Salvo salvo8 = new Salvo(4,location8,game1Chloe);
////            Salvo salvo2 = new Salvo(2,location6,game1Chloe);
////            Salvo salvo3 = new Salvo(1,location7,game2kim);
////            Salvo salvo4 = new Salvo(2,location8,game2david);
//            salvoRepository.save(salvo1);
//            salvoRepository.save(salvo2);
//            salvoRepository.save(salvo3);
//            salvoRepository.save(salvo4);
//
//            salvoRepository.save(salvo5);
//            salvoRepository.save(salvo6);
//            salvoRepository.save(salvo7);
//            salvoRepository.save(salvo8);
//
//            Score score1 = new Score(0.5, game1, jack);
//            Score score2 = new Score(1.0, game1, jack);
//            Score score3 = new Score(0.0, game3, jack);
//
//            scoreRepository.save(score1);
//            scoreRepository.save(score2);
//            scoreRepository.save(score3);

//




       };
    }




}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
    @Autowired
    PlayerRepository playerRepository;

    @Override

    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inputName-> {
            Player player = playerRepository.findByUser(inputName);
            if (player != null) {
                return new User(player.getUser(), player.getPassword(),

                        AuthorityUtils.createAuthorityList("USER"));
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputName);
            }
        });
    }
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**").permitAll()
//                .antMatchers("/api/login").permitAll()
//                .antMatchers("/api/logout").permitAll()
//                .antMatchers("/api/players").permitAll()
//                .antMatchers("/web/games.html").permitAll()
////                .antMatchers("/api/game_view/{gpId}").permitAll()
////                .antMatchers("/game/{gameId}/players").permitAll()
//                .antMatchers("/web/game.html").fullyAuthenticated()
//                .antMatchers("/rest/**").denyAll()
                .and()
                .formLogin();

        http.formLogin()
                .usernameParameter("user")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");

        // turn off checking for CSRF tokens
        http.csrf().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }
    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}

