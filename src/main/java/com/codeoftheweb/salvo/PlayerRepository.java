package com.codeoftheweb.salvo;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;

import java.util.List;

@RepositoryRestResource
 public interface PlayerRepository extends JpaRepository<Player, Long> {
//    List<Player> findByUser(String user);
    Player findByUser(@Param("user") String user);

}
