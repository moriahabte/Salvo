

function nextChar(c) {
    return String.fromCharCode(c.charCodeAt(0) + 1);
}
function formerChar(c) {
    return String.fromCharCode(c.charCodeAt(0) - 1);
}
function allowDrop(ev) {
    
    var t = event.target;
    // Find the drop target
    while (t !== null && !t.classList.contains("target")) {
        t = t.parentNode;
    }
    // If the target is empty allow the drop.
    if (t && t.childNodes.length == 0) {
        event.preventDefault();
    }
    return false;
//    ev.preventDefault();
    
}

function drag(ev) {
    
    ev.dataTransfer.setData("text", ev.target.id);
}

function drop(ev) {
    
    ev.preventDefault();
    var data = ev.dataTransfer.getData("text");
    
   
   var elementIdNumber =  parseInt(data.split(/(?=[1-9])/)[1], 10);
    var elementIdName =data.split(/(?=[1-9])/)[0];
    console.log(elementIdName);
    console.log(elementIdNumber);
    ev.target.appendChild(document.getElementById(data));
   
    
    var letterNumberArray = ev.target.id.split(/(?=[1-9])/);
    console.log(letterNumberArray[0] );
    
    var number = letterNumberArray[1];
    app.letter = letterNumberArray[0];
//    console.log(number);
app.integer = parseInt(number, 10);
    
//    console.log(app.integer + 1);
    console.log(letterNumberArray[0]);
    
    if(app.rotateShip1 == false){

        if(document.getElementById(app.letter + (app.integer+1)) != null){
           document.getElementById(app.letter + (app.integer+1)).appendChild(document.getElementById(elementIdName + (elementIdNumber + 1)));
           }else{
               document.getElementById(app.letter + (app.integer-1)).appendChild(document.getElementById(elementIdName + (elementIdNumber + 1)));
           }
        
    }else{
        if(document.getElementById(nextChar(app.letter) + (app.integer)) != null){
             document.getElementById(nextChar(app.letter) + (app.integer)).appendChild(document.getElementById(elementIdName + (elementIdNumber + 1)));
        }else{
            document.getElementById(formerChar(app.letter) + (app.integer)).appendChild(document.getElementById(elementIdName + (elementIdNumber + 1)));
        }
        
    }

    
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
        salvoTurn: 0,
        salvoTurn2: 0,
        rotateShip1: false,
        letter: 0,
        integer: 0,
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

                    //                app.getShips();

                    console.log(myJson);
                    data = myJson;
                    this.gameView = data;

                    console.log(app.id);


                    //make the player1 the the gameview gameplayer email
                    app.userIsPlayer1(data);


                    //make shipLocations td turn purple with ship class
                    //                app.ShowShipLocations();
                    //
                    //                //make salvolocations td turn green with salvo class
                    //                app.showSalvolocations();
                    //
                    //                /*make opponenSalvolocations td turn orange with opponentSalvo
                    //                make shipLocation that got hit purlple with yougothit*/
                    //                app.showOpponenSalvolocations();
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
            //            console.log(data.ships["0"].type);
            wang = data.ships;
            shipLocations = [];
            console.log(wang);

            //            for (let i = 0; i < wang.length; i++) {
            //                shipLocations = data.ships[i].location;
            //                console.log(shipLocations);
            //                for (let j = 0; j < shipLocations.length; j++) {
            //                    document.getElementById(data.ships[i].location[j]).classList.add("ship");
            //                }
            //            }


        },
        showSalvolocations: function () {
            raw = data.salvoes;
            console.log(raw);

            //            for (let i = 0; i < raw.length; i++) {
            //                salvoLocation = data.salvoes[i].locations;
            //                console.log(salvoLocation);
            //                app.salvoTurn = data.salvoes[i].turn;
            //                for (let j = 0; j < salvoLocation.length; j++) {
            //                    document.getElementById("s" + data.salvoes[i].locations[j]).classList.add("salvo");
            //                }
            //            }
        },
        showOpponenSalvolocations: function () {
            opponentSalvoes = data.opponentSalvoes;
            console.log(opponentSalvoes);

            //            for (let i = 0; i < opponentSalvoes.length; i++) {
            //
            //                opponentSalvoLocation = data.opponentSalvoes[i].locations;
            //                console.log(opponentSalvoLocation);
            //                app.salvoTurn2 = data.opponentSalvoes[i].turn;
            //
            //                for (let j = 0; j < opponentSalvoLocation.length; j++) {
            //                    let cell = document.getElementById(opponentSalvoLocation[j]);
            //                    if (cell.classList.contains("ship")) {
            //                        cell.classList.remove('ship')
            //                        cell.classList.add('youGotHit')
            //                    } else {
            //                        cell.classList.add("opponentSalvo");
            //                    }
            //                }
            //            }
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
            fetch("/api/games/players/" + this.id + "/ships", {
                    credentials: 'include',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },

                    body: JSON.stringify([{
                            shipType: "battleship",
                            shipLocation: ["A1", "A6", "A8"]
               },
                        {
                            shipType: "oilship",
                            shipLocation: ["B1", "B6", "B8", "B9"]
               },
                                         ])
                })
                .then(r => r.json())
                .then(r => {
                    console.log(r);
                    location.reload();

                })
                .catch(r => console.log(r))
        },
         turnShip1: function(){
             
    if(app.rotateShip1 == true){
        app.rotateShip1 = false;
        console.log("false")
         if(document.getElementById(this.letter + (app.integer+1)) != null){
           document.getElementById(this.letter + (app.integer+1)).appendChild(document.getElementById("drag2"));
           }else{
               document.getElementById(this.letter + (app.integer-1)).appendChild(document.getElementById("drag2"));
           }
        
    }else{
        app.rotateShip1 = true;
        console.log("true");
        if(document.getElementById(nextChar(this.letter) + (app.integer)) != null){
             document.getElementById(nextChar(this.letter) + (app.integer)).appendChild(document.getElementById("drag2"));
        }else{
            document.getElementById(formerChar(this.letter) + (app.integer)).appendChild(document.getElementById("drag2"));
        }
    }
},


    }

})
