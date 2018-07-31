package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @RequestMapping("/game_view/{gamePlayerId}")
    public Map<String, Object> getGameView(@PathVariable Long gamePlayerId){
        Map<String, Object> gameView = new LinkedHashMap<String, Object>();

        GamePlayer gamePlayer = gamePlayerRepository.findOne(gamePlayerId);
        GamePlayer otherPlayer;
                ;

        for(int i = 0; i < gamePlayer.getGame().getGamePlayers().length; ++i)
        for(GamePlayer famePlayer: gamePlayer.getGame().getGamePlayers()){
            if(  famePlayer.getId() != gamePlayer.getId()){
                GamePlayer otherPlayer =  famePlayer;
            }
        }

        gameView.put("game",makeGameDTO(gamePlayer.getGame()));

        gameView.put("ships", gamePlayer.getShips()
                .stream()
                .map(ship -> makeShipDTO(ship))
                .collect(Collectors.toList()));


        gameView.put("salvoes", gamePlayer.getSalvoes()
                .stream()
                .map(salvo -> makeSalvoDTO(salvo))
                .collect(Collectors.toList()));

        gameView.put("salvoesOpponent", otherPlayer.getSalvoes()
                        .stream()
                        .map(salvo -> makeSalvoDTO(salvo))
                        .collect(Collectors.toList()));



            return gameView;
    }


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
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getId());
        //dto.put("joinDate", gamePlayer.getJoinDate());
        dto.put("player", makePlayerDTO(gamePlayer.getPlayer()));


        return dto;
    }

    private Map<String, Object> makePlayerDTO(Player player) {
       Map<String, Object> dto = new LinkedHashMap<String, Object>();
       dto.put("id", player.getId());
       dto.put("email", player.getUser());

       return dto;
    }

    private Map<String, Object> makeShipDTO (Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", ship.getShipType());
        dto.put("location", ship.getShipLocation());


        return dto;


    }

    private Map<String, Object> makeSalvoDTO(Salvo salvo){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turn", salvo.getTurnNumber());
        dto.put("player", salvo.getGamePlayer().getId());
        dto.put("locations", salvo.getSalvoLocations());

        return dto;
    }


}
