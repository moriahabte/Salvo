package com.codeoftheweb.salvo;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Id;
import java.util.Date;
import java.util.List;

public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {


}
