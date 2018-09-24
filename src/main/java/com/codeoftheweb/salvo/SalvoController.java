package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private SalvoRepository salvoRepository;

    private GamePlayer getOtherPlayer(GamePlayer gamePlayer){
        List<GamePlayer> gamePlayersList = new ArrayList<>();
        Set<GamePlayer> gamePlayerSet = gamePlayer.getGame().getGamePlayers();
        for (GamePlayer gp : gamePlayerSet) {
            if (gp != gamePlayer) {
                gamePlayersList.add(gp);
            }
        }
        return gamePlayersList.get(0);
    }


    @RequestMapping(path = "/login", method = RequestMethod.GET)
    private Player getAll(Authentication authentication) {
        return playerRepository.findByUser(authentication.getName());
    }




    @RequestMapping("/leaderBoard")
    private List<Object> getAllScores() {
        return playerRepository
                .findAll()
                .stream()
                .map(player -> makePlayerDTO(player))
                .collect(Collectors.toList());
    }

    @RequestMapping("/game_view/{gamePlayerId}")
    private Map<String, Object> getGameView(@PathVariable Long gamePlayerId, Authentication authentication){
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

        if(gamePlayer.getGame().getGamePlayers().size() == 2) {
            gameView.put("opponentSalvoes", otherPlayer.getSalvoes()
                    .stream()
                    .map(salvo -> makeSalvoDTO(salvo))
                    .collect(Collectors.toList()));


            gameView.put("hits", getHits(gamePlayer));

        }

        ArrayList<String> salvoLocations = new ArrayList<String>();
        gamePlayer.getSalvoes()
                .stream()
                .map(salvo -> salvoLocations.addAll(salvo.getSalvoLocations()));


            if(gamePlayer.getPlayer().getUser() == authentication.getName()){

                return gameView;
            }else{

                return makeMap("error", "Not allowed");
            }
    }




    @RequestMapping(path = "/players", method = RequestMethod.POST)
    private ResponseEntity<Map<String, Object>> createUser(@RequestParam String user, String password) {
        if (user.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "No name"), HttpStatus.FORBIDDEN);
        } else {

            Player player = playerRepository.findByUser(user);
            if (player != null) {
                return new ResponseEntity<>(makeMap("error", "Username already exists"), HttpStatus.CONFLICT);
            } else {
                Player newPlayer = playerRepository.save(new Player(user, password));
                return new ResponseEntity<>(makeMap("id", newPlayer.getId()), HttpStatus.CREATED);
            }
        }


    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }




    @RequestMapping(path = "/game/{gameId}/players", method = RequestMethod.POST)
        private ResponseEntity<Map<String,Object>> joinGame (@PathVariable Long gameId, Authentication authentication) {
        Game game = gameRepository.findOne(gameId);
        Player player = playerRepository.findByUser(authentication.getName());
        GamePlayer gamePlayer = new GamePlayer(game, player);
            if (authentication != null){
                if(game.getGamePlayers().size() == 1) {
                    gamePlayerRepository.save(gamePlayer);
                    return new ResponseEntity<>(makeMap("gpId", gamePlayer.getId()), HttpStatus.CREATED);
                }else{
                    return new ResponseEntity<>(makeMap("error", "game is full"), HttpStatus.FORBIDDEN);
                }

            }else{
                return new ResponseEntity<>(makeMap("error", "not logged"), HttpStatus.UNAUTHORIZED);
            }
        }

        private Integer getTurn(GamePlayer gamePlayer){
         Integer lastTurn = 0;
         for (Salvo salvo : gamePlayer.getSalvoes()) {
             Integer turn = salvo.getTurnNumber();
             if (lastTurn < turn) {
                 lastTurn = turn;
             }
         }
        return lastTurn + 1;
     }

    @RequestMapping(path = "/games/players/{gamePlayerId}/salvos", method = RequestMethod.POST)
    private ResponseEntity<Map<String,Object>> addSalvoes(@PathVariable Long gamePlayerId,
                                                          Authentication authentication,
                                                          @RequestBody Salvo salvo) {

        GamePlayer gamePlayer = gamePlayerRepository.findOne(gamePlayerId);
        Player player = playerRepository.findByUser(authentication.getName());
        Game game = gameRepository.findOne(gamePlayer.getGame().getId());

        if (authentication != null) {
            if(gamePlayer != null){
                if(gamePlayer.getPlayer().getUser() == player.getUser()) {
                    salvo.setGamePlayer(gamePlayer);
                    salvo.setTurnNumber(getTurn(gamePlayer));
                    salvoRepository.save(salvo);

                    return new ResponseEntity<>(makeMap("succeed", "salvo done"), HttpStatus.CREATED);
                }else{
                    return new ResponseEntity<>(makeMap("error", "not logged in"), HttpStatus.UNAUTHORIZED);
                }
            }else{
                return new ResponseEntity<>(makeMap("error", "not allowed"), HttpStatus.UNAUTHORIZED);
            }
        }else{
            return new ResponseEntity<>(makeMap("error", "not logged in"), HttpStatus.UNAUTHORIZED);
        }
    }



    @RequestMapping(path = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
        private ResponseEntity<Map<String,Object>> addShips(@PathVariable Long gamePlayerId, Authentication authentication, @RequestBody Set<Ship> ships) {
        GamePlayer gamePlayer = gamePlayerRepository.findOne(gamePlayerId);
        Player player = playerRepository.findByUser(authentication.getName());
        if (authentication != null) {
            if(player.getGamePlayers().contains(gamePlayer)){

                if(gamePlayer.getShips().size() == 0){
                    for (Ship ship : ships) {
                        ship.setGamePlayer(gamePlayer);
                        shipRepository.save(ship);
                    }

                    return new ResponseEntity<>(makeMap("success", "ships created"), HttpStatus.CREATED);
                }else{
                    return new ResponseEntity<>(makeMap("error", "already added ships"), HttpStatus.FORBIDDEN);

                }

            }else{
                return new ResponseEntity<>(makeMap("error", "not allowed"), HttpStatus.UNAUTHORIZED);

            }

        }else{
            return new ResponseEntity<>(makeMap("error", "not logged in"), HttpStatus.UNAUTHORIZED);
        }
    }


    @RequestMapping(path = "/games", method = RequestMethod.POST)
        private ResponseEntity<Map<String,Object>> createGame(Authentication authentication) {
            if (authentication != null) {
                Game game = new Game();
                gameRepository.save(game);
                Player player = playerRepository.findByUser(authentication.getName());
                GamePlayer gamePlayer = new GamePlayer(game, player);
                gamePlayerRepository.save(gamePlayer);

                return new ResponseEntity<>(makeMap("succes", gamePlayer.getId()), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(makeMap("error", "log in"), HttpStatus.UNAUTHORIZED);
            }
        }



        @RequestMapping(path = "/games",method = RequestMethod.GET)
        private Map<String, Object> allGames(Authentication authentication){
            Map<String, Object> dto = new LinkedHashMap<String, Object>();
            if(authentication != null){
                dto.put("player",makeCurrentUserDTO(authentication));
                dto.put("games", getAllGames());
            } else {
                dto.put("games", getAllGames());
            }

            return dto;

        }
        private Map<String, Object> makeCurrentUserDTO(Authentication authentication){
            Map<String, Object> dto = new LinkedHashMap<String, Object>();
            dto.put("id", getAll(authentication).getId());
            dto.put("name", getAll(authentication).getUser());

            return dto;
        }

        private List<Object> getAllGames() {
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

            dto.put("gpId", gamePlayer.getId());



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

        private List<String> getOpponentshipLocations(GamePlayer gamePlayer) {
        GamePlayer otherPlayer = getOtherPlayer(gamePlayer);
            List shipLocationsList = new ArrayList();
            for (Ship ship : otherPlayer.getShips()) {
                for ( String shiplocation : ship.getShipLocation()){
                    shipLocationsList.add(shiplocation);
                }
            }

            return shipLocationsList;

        }

        private List<String> salvoLocations(GamePlayer gamePlayer) {
            List salvoLocationsList = new ArrayList();
            for (Salvo salvo : gamePlayer.getSalvoes()) {
                for ( String salvoLocation : salvo.getSalvoLocations()){
                    salvoLocationsList.add(salvoLocation);
                }
            }

            return salvoLocationsList;

        }

        private List <String> getHits(GamePlayer gamePlayer){
          List<String> salvoLocations = salvoLocations(gamePlayer);
          List<String> opponentShipLocations = getOpponentshipLocations(gamePlayer);
          List<String> hits = new ArrayList<>();

          for(String salvoLocation : salvoLocations){
              for(String opponentShipLocation : opponentShipLocations){
                  if(salvoLocation == opponentShipLocation){
                      hits.add(salvoLocation);
                  }
              }
          }
            return hits;
        }

        private List<String> getSinks (GamePlayer gamePlayer){
            List<String> sinks = new ArrayList<>();
        return sinks;
        }


    }


