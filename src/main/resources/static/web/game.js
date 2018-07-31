var app = new Vue({
    el: '#app',
    data: {
        message: 'Hello Vue!',
        id: location.search.split("=")[1],
        gameView: {},
        shipLocation: [],
        player1: 0,
        player2: 0,
        salvoTurn: 0
    },
    created: function(){
        //get view of one  gamePlayer by id
        fetch("/api/game_view/" + this.id,  {
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
            for(let i=0;i<2;i++){
                if(data.game.gamePlayers[i].player.id == app.id){
                    app.player1 = data.game.gamePlayers[i].player.email;
                }else{
                    app.player2 = data.game.gamePlayers[i].player.email;
                }
            }
            

            //make shipLocations td turn red
            console.log(data.ships["0"].type);
            wang = data.ships;
            shipLocations = [];
            console.log(wang);
            
            for(let i=0;i<wang.length;i++){
                shipLocations = data.ships[i].location;
                console.log(shipLocations);
                for(let j=0;j<shipLocations.length;j++){
                     document.getElementById(data.ships[i].location[j]).setAttribute("class", "purple");
                }
            }
            
            //make salvolocations td turn green
            raw = data.salvoes;
            console.log(raw);
            
            for(let i=0;i<raw.length;i++){
                salvoLocation = data.salvoes[i].locations;
                console.log(salvoLocation);
                app.salvoTurn = data.salvoes[i].turn;
                for(let j=0;j<salvoLocation.length;j++){
                     document.getElementById("s" + data.salvoes[i].locations[j]).setAttribute("class", "green");
                }
            }
            
            
                  
            
            
            });
        
        
        


    },
    methods: {
        

        }
        
    })
    



