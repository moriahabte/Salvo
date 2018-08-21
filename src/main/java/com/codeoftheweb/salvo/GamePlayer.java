package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Date joinDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    Set<Ship> ships = new LinkedHashSet<>();

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
        Set<Salvo> salvoes = new LinkedHashSet<>();

    public Set<Ship> getShips() {
        return ships;
    }

    public Set<Salvo> getSalvoes() { return salvoes; }

    public void addShip(Ship ship) {
//        ship.setGamePlayer(this);
        ships.add(ship);
    }

//    public void addSalvo(Salvo salvo){
//        salvo.setGamePlayer(this);
//        salvoes.add(salvo);
//    }

    public GamePlayer() {
    }

    public GamePlayer(Game game, Player player) {
        this.joinDate = new Date();
        this.game = game;
        this.player = player;

    }

    public Date getJoinDate() {
        return joinDate;
    }

    @JsonIgnore
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }


    @JsonIgnore
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public long getId() {
        return id;
    }

    public Score getScore(){
        return player.getScores()
                .stream()
                .filter(s -> s.getGame() == game)
                .findFirst().orElse(null);

//        return player.getScore(game);
    }
}


