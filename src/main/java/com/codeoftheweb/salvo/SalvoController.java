package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {
    @Autowired
    private GameRepository gameRepository;

    @RequestMapping("/games")
    public List<Object> getAllGames() {
        return gameRepository
                .findAll()
                .stream()
                .map(game -> makeGameDTO(game))
                .collect(Collectors.toList());
    }


    private Map<String, Object> makeGameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("creationDate", game.getCreationDate());
        dto.put("gamePlayers", game.getGamePlayers()
                .stream()
                .map(gp->makeGamePlayerDTO(gp))
                .collect(Collectors.toList()));
        return dto;
    }

    private Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto2 = new LinkedHashMap<String, Object>();
        dto2.put("id", gamePlayer.getId());
        //dto.put("joinDate", gamePlayer.getJoinDate());
        dto2.put("player", makePlayerDTO(gamePlayer.getPlayer()));


        return dto2;
    }

    private Map<String, Object> makePlayerDTO(Player player) {
       Map<String, Object> dto = new LinkedHashMap<String, Object>();
       dto.put("id", player.getId());
       dto.put("email", player.getUser());

       return dto;
    }


}
