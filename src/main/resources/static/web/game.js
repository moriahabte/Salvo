var hits = [

    {
        "turn": 1,
        "newHits": 5,
        "sinks": ["battleship"],
    }, 
    {
        "turn": 2,
        "newHits": 3,
        "sinks": ["battleship", "submarine"],
    }, 
    {
        "turn": 3,
        "newHits": 5,
        "sinks": ["battleship", "submarine", "boat"],
    }, 
]
var opponentHits = [

    {
        "turn": 1,
        "newHits": 5,
        "sinks": ["battleship"],
    }, 
    {
        "turn": 3,
        "newHits": 3,
        "sinks": ["battleship", "submarine"],
    }, 
    {
        "turn": 3,
        "newHits": 5,
        "sinks": ["battleship", "submarine", "boat"],
    }, 
]

    
    
console.log(hits[hits.length-1].turn);
console.log(opponentHits);
hits[hits.length-1].turn

var shipType = "";

function drag(ev) {
    shipType = ev.target.classList[0];
    ev.dataTransfer.setData("text", ev.target.id);
}

function nextChar(c) {
    return String.fromCharCode(c.charCodeAt(0) + 1);
}

function formerChar(c) {
    return String.fromCharCode(c.charCodeAt(0) - 1);
}

function allowDrop(ev) {
    var shipLength = document.getElementsByClassName(shipType).length;
    var t = event.target;
    var b = event.target.id;
    var elementIdNumber = parseInt(b.split(/(?=[1-9])/)[1], 10);
    var elementIdName = b.split(/(?=[1-9])/)[0];
    var dropable = true;
    // Find the drop target
    while (t !== null && !t.classList.contains("target")) {
        t = t.parentNode;
    }
    // If the targets are empty allow the drop.
    if (t && t.childNodes.length == 0) {
        for (var i = 1; i < shipLength; i++) {
            if (app.rotateShip1 == false) {
                if (document.getElementById(elementIdName + (elementIdNumber + i)).childNodes.length != 0) {
                    dropable = false;
                }
            } else {
                if (document.getElementById(String.fromCharCode(elementIdName.charCodeAt(0) + i) + elementIdNumber).childNodes.length != 0) {
                    dropable = false;
                }
            }
        }
        if (dropable) {
            
            event.preventDefault();
        }
    }
    return false;
}

function drop(ev) {
    var b = ev.target.childNodes
    ev.preventDefault();
    var data = ev.dataTransfer.getData("text");
    var elementIdNumber = parseInt(data.split(/(?=[1-9])/)[1], 10);
    var elementIdName = data.split(/(?=[1-9])/)[0];
    ev.target.appendChild(document.getElementById(data));
    var letterNumberArray = ev.target.id.split(/(?=[1-9])/);
    var number = letterNumberArray[1];
    app.letter = letterNumberArray[0];
    app.integer = parseInt(number, 10);
    var ships = document.getElementsByClassName(shipType);
    var shipLength = document.getElementsByClassName(shipType).length;

    console.log(shipType);
    console.log(shipLength);

    for (var i = 0; i < shipLength; i++) {

        if (app.rotateShip1 == false) {
            document.getElementById(app.letter + (app.integer + i)).appendChild(document.getElementById(elementIdName + (elementIdNumber + i)));
            console.log(shipType)
        } else {
            document.getElementById(String.fromCharCode(app.letter.charCodeAt(0) + i) + (app.integer)).appendChild(document.getElementById(elementIdName + (elementIdNumber + i)));
        }

    }
}

function returnShipLocation(shipClass) {
    var battleShipLocation = [];
    var battleShipLength = document.getElementsByClassName(shipClass).length;

    for (var i = 0; i < battleShipLength; i++) {
        battleShipLocation.push(document.getElementsByClassName(shipClass)[i].parentElement.id)
    }
    return battleShipLocation
}


var app = new Vue({
    el: '#app',
    data: {
        message: 'Hello Vue!',
        id: location.search.split("=")[1],
        gameView: {},
        shipLocation: [],
        player1: 0,
        player2: 0,
        salvoTurn: hits[hits.length-1].turn,
        salvoTurn2: 0,
        rotateShip1: false,
        letter: 0,
        integer: 0,
        dropable: true,
    },
    created: function () {
        //        this.postShips();
        this.getGameView();

    },
    methods: {
        getGameView: function () {
            //get view of one  gamePlayer by id
            fetch("/api/game_view/" + this.id, {
                    method: "GET",
                    credentials: "include",
                }).then(function (response) {
                    return response.json();
                })
                .then(function (myJson) {

                    //app.getShips();

                    console.log(myJson);
                    data = myJson;
                    this.gameView = data;

                    console.log(app.id);

                    //make the player1 the the gameview gameplayer email
                    app.userIsPlayer1(data);

                    console.log(data.ships.length);
                    
                    // loop through all ships and show
                    for ( i = 0; i < data.ships.length; i++) {
                        
                        for ( j = 0; j < data.ships[i].location.length; j++) {
                         
                                shiplocation = data.ships[i].location[j];
                                shipPart = document.getElementsByClassName(data.ships[i].type + "-" + j);
                                document.getElementById(shiplocation).appendChild(shipPart[0]);
                            }
                    }
                    //loop through salvoes to show
                    for(i=0;i<data.salvoes.length;i++){
                        for(j=0;j<data.salvoes[i].locations.length;j++){
                            document.getElementById("s" + data.salvoes[i].locations[j]).classList.add("lastTurn");
                        }
                    }
                });

        },
        userIsPlayer1: function (data) {
            if (data.game.gamePlayers.length == 2) {

                if (data.game.gamePlayers[0].gpId == this.id) {
                    app.player1 = data.game.gamePlayers[0].player.email;
                    app.player2 = data.game.gamePlayers[1].player.email;
                } else {
                    app.player1 = data.game.gamePlayers[1].player.email;
                    app.player2 = data.game.gamePlayers[0].player.email;
                }
            } else {
                app.player1 = data.game.gamePlayers[0].player.email;
                app.player2 = "Waiting for Enemy";
            }

        },
        ShowShipLocations: function () {
            wang = data.ships;
            shipLocations = [];
            console.log(wang);


        },
        showSalvolocations: function () {
            raw = data.salvoes;
            console.log(raw);


        },
        showOpponenSalvolocations: function () {
            opponentSalvoes = data.opponentSalvoes;
            console.log(opponentSalvoes);


        },
        logOut: function () {
            fetch("/api/logout", {
                    credentials: 'include',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },

                })
                .then(r => {
                    if (r.status == 200) {
                        this.alertDiv = false;
                        this.logged = false;
                    } else {
                        this.alertDiv = false;
                        this.logged = true;
                    }
                    console.log(r)
                })
                .catch(r => console.log(r)).done(window.location.href = "/web/games.html")

        },
        goBack: function () {
            history.back();
        },
        postShips: function () {
            post = true;
            shipDock = document.getElementsByClassName("div1").length;

            for (i = 0; i < shipDock; i++) {
                if (document.getElementsByClassName("div1")[i].childElementCount != 0) {
                    post = false;
                }
            }

            if (post) {



                fetch("/api/games/players/" + this.id + "/ships", {
                        credentials: 'include',
                        method: 'POST',
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },

                        body: JSON.stringify([
                            {
                                shipType: "battleship",
                                shipLocation: returnShipLocation("battleship"),
               },
                            {
                                shipType: "boat",
                                shipLocation: returnShipLocation("boat"),
               },
                            {
                                shipType: "destroyer",
                                shipLocation: returnShipLocation("destroyer"),
               },
                            {
                                shipType: "submarine",
                                shipLocation: returnShipLocation("submarine"),
               },
                            {
                                shipType: "aircraftcarrier",
                                shipLocation: returnShipLocation("aircraftcarrier"),
               },
                                         ])
                    })
                    .then(r => r.json())
                    .then(r => {
                        console.log(r);
                        app.getGameView();
                        //                        location.reload();

                    })
                    .catch(r => console.log(r))
            }
        },
        turnShip1: function (id) {
            //            data = ev.dataTransfer.getData("text");
            elementIdNumber = parseInt(id.split(/(?=[1-9])/)[1], 10);
            elementIdLetter = id.split(/(?=[1-9])/)[0];
            full = console.log(document.getElementById(id).parentNode.id);
            letter = document.getElementById(id).parentNode.id.split(/(?=[1-9])/)[0];

            shipLength = document.getElementsByClassName(shipType).length;

            number = document.getElementById(id).parentNode.id.split(/(?=[1-9])/)[1];
            integer = parseInt(number.split(/(?=[1-9])/)[0], 10);

            app.dropable = true;






            if (app.rotateShip1 == true) {
                for (var i = 1; i < shipLength; i++) {
                    console.log(document.getElementById(letter + (integer + i)).childNodes.length);
                    if (document.getElementById(letter + (integer + i)).childNodes.length !== 0) {
                        app.dropable = false;
                        console.log("flipped vertical")
                    }
                }
            } else if (app.rotateShip1 == false) {
                for (var i = 1; i < shipLength; i++) {
                    if (document.getElementById(String.fromCharCode(letter.charCodeAt(0) + i) + (integer)).childNodes.length !== 0) {
                        console.log("whadup");
                        app.dropable = false;
                    }
                }
            }
            console.log(app.dropable);

            if (app.dropable == true) {

                if (app.rotateShip1 == false) {


                    for (var i = 1; i < shipLength; i++) {
                        document.getElementById(String.fromCharCode(letter.charCodeAt(0) + i) + (integer)).appendChild(document.getElementById(elementIdLetter + (elementIdNumber + i)));
                    }
                    app.rotateShip1 = true;
                } else {


                    for (var i = 1; i < shipLength; i++) {
                        document.getElementById(letter + (integer + i)).appendChild(document.getElementById(elementIdLetter + (elementIdNumber + i)));
                    }
                    app.rotateShip1 = false;
                }
            }




        },
        showPosition: function () {
            post = true;
            shipDock = document.getElementsByClassName("div1").length;

            for (i = 0; i < shipDock; i++) {
                if (document.getElementsByClassName("div1")[i].childElementCount != 0) {
                    post = false;
                }
            }
            console.log(post);



        },
        salvoShots: function (id) {
            var element = document.getElementById(id);
            if (!element.classList.contains("lastTurn")){
                element.classList.toggle("salvoLocation");
            }
            if(document.getElementsByClassName("salvoLocation").length > 5){
                document.getElementsByClassName("salvoLocation")[0].classList.toggle("salvoLocation");
            }

            
        },
        postSalvo: function() {
            salvos = []
            if(document.getElementsByClassName("salvoLocation").length == 5){
            for(i=0;i<document.getElementsByClassName("salvoLocation").length;i++){
                salvos.push(document.getElementsByClassName("salvoLocation")[i].id.split("s")[1]);
//                            .split("s")[1]
            }
            console.log(salvos);
            
            fetch("/api/games/players/" + this.id + "/salvos" , {
                        credentials: 'include',
                        method: 'POST',
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },

                        body: JSON.stringify(
                            {
                                salvoLocations: salvos ,
               },
                          
                                         )
                    })
                    .then(r => r.json())
                    .then(r => {
                        console.log(r);
                        app.getGameView();
                        //                        location.reload();

                    })
                    .catch(r => console.log(r))
        }
        },


    }

})
