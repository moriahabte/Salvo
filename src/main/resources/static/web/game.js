var app = new Vue({
    el: '#app',
    data: {
        message: 'Hello Vue!',
        id: location.search.split("=")[1],
        gameView: {},
        shipLocation: [],
        player1: 0,
        player2: 0,
        salvoTurn: 0,
        salvoTurn2: 0
    },
    created: function () {
        //get view of one  gamePlayer by id
        fetch("/api/game_view/" + this.id, {
                method: "GET",
                credentials: "include",
            }).then(function (response) {
                return response.json();
            })
            .then(function (myJson) {
                console.log(myJson);
                data = myJson;
                app.gameView = data;

                console.log(app.id);

                //make the player1 the the gameview gameplayer email
                app.userIsPlayer1();


                //make shipLocations td turn purple with ship class
                app.ShowShipLocations();

                //make salvolocations td turn green with salvo class
                app.showSalvolocations();

                /*make opponenSalvolocations td turn orange with opponentSalvo
                make shipLocation that got hit purlple with yougothit*/
                app.showOpponenSalvolocations();
            });
    },
    methods: {
        userIsPlayer1: function () {
            for (let i = 0; i < 2; i++) {
                if (data.game.gamePlayers[i].player.id == app.id) {
                    app.player1 = data.game.gamePlayers[i].player.email;
                } else {
                    app.player2 = data.game.gamePlayers[i].player.email;
                }
            }

        },
        ShowShipLocations: function () {
            console.log(data.ships["0"].type);
            wang = data.ships;
            shipLocations = [];
            console.log(wang);

            for (let i = 0; i < wang.length; i++) {
                shipLocations = data.ships[i].location;
                console.log(shipLocations);
                for (let j = 0; j < shipLocations.length; j++) {
                    document.getElementById(data.ships[i].location[j]).classList.add("ship");
                }
            }


        },
        showSalvolocations: function () {
            raw = data.salvoes;
            console.log(raw);

            for (let i = 0; i < raw.length; i++) {
                salvoLocation = data.salvoes[i].locations;
                console.log(salvoLocation);
                app.salvoTurn = data.salvoes[i].turn;
                for (let j = 0; j < salvoLocation.length; j++) {
                    document.getElementById("s" + data.salvoes[i].locations[j]).classList.add("salvo");
                }
            }
        },
        showOpponenSalvolocations: function () {
            opponentSalvoes = data.opponentSalvoes;
            console.log(opponentSalvoes);

            for (let i = 0; i < opponentSalvoes.length; i++) {

                opponentSalvoLocation = data.opponentSalvoes[i].locations;
                console.log(opponentSalvoLocation);
                app.salvoTurn2 = data.opponentSalvoes[i].turn;

                for (let j = 0; j < opponentSalvoLocation.length; j++) {
                    let cell = document.getElementById(opponentSalvoLocation[j]);
                    if (cell.classList.contains("ship")) {
                        cell.classList.remove('ship')
                        cell.classList.add('youGotHit')
                    } else {
                        cell.classList.add("opponentSalvo");
                    }
                }
            }
        }
    }

})
