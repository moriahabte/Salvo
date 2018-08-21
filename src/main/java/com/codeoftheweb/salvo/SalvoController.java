package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api")
public class SalvoController {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private PlayerRepository playerRepository;



    public GamePlayer getOtherPlayer(GamePlayer gamePlayer){
        List<GamePlayer> gamePlayersList = new ArrayList<>();
        Set<GamePlayer> gamePlayerSet = gamePlayer.getGame().getGamePlayers();
        for (GamePlayer gp : gamePlayerSet) {
            if (gp != gamePlayer) {
                gamePlayersList.add(gp);
            }
        }
        return gamePlayersList.get(0);
    }


    @RequestMapping("/login")
    public Player getAll(Authentication authentication) {
        return playerRepository.findByUser(authentication.getName());
    }




    @RequestMapping("/leaderBoard")
    public List<Object> getAllScores() {
        return playerRepository
                .findAll()
                .stream()
                .map(player -> makePlayerDTO(player))
                .collect(Collectors.toList());
    }

   @RequestMapping("/game_view/{gamePlayerId}")
    public Map<String, Object> getGameView(@PathVariable Long gamePlayerId){
        Map<String, Object> gameView = new LinkedHashMap<String, Object>();

        GamePlayer gamePlayer = gamePlayerRepository.findOne(gamePlayerId);
        GamePlayer otherPlayer = getOtherPlayer(gamePlayer);




        gameView.put("game",makeGameDTO(gamePlayer.getGame()));

        gameView.put("ships", gamePlayer.getShips()
                .stream()
                .map(ship -> makeShipDTO(ship))
                .collect(Collectors.toList()));


        gameView.put("salvoes", gamePlayer.getSalvoes()
                .stream()
                .map(salvo -> makeSalvoDTO(salvo))
                .collect(Collectors.toList()));

        gameView.put("opponentSalvoes", otherPlayer.getSalvoes()
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
        if (gamePlayer.getScore() != null) {
            dto.put("scores", makeScoreDTO(gamePlayer.getScore()));
        }


        return dto;
    }

    private Map<String, Object> makePlayerDTO(Player player) {
       Map<String, Object> dto = new LinkedHashMap<String, Object>();
       dto.put("id", player.getId());
       dto.put("email", player.getUser());
        double sum = 0.0;
        int countWins = 0;
        int countTies = 0;
        int countLosses = 0;
      Set<Score> scores = player.getScores();
        for (Score score : scores) {
            sum += score.getScore();
            if(score.getScore() == 1.0 ){
                countWins += 1;
            }
            if(score.getScore() == 0.5){
                countTies += 1;
            }
            if(score.getScore() == 0.0){
                countLosses += 1;
            }
        }



        dto.put("totalScore", sum);
        dto.put("wins", countWins);
        dto.put("ties", countTies);
        dto.put("losses", countLosses);



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

    private Map<String, Object> makeScoreDTO(Score score){
            Map<String, Object> scoreDTO = new LinkedHashMap<String, Object>();
        scoreDTO.put("score", score.getScore());

            return scoreDTO;
        }




}
