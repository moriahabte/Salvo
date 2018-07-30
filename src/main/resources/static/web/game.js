var app = new Vue({
    el: '#app',
    data: {
        message: 'Hello Vue!',
        id: location.search.split("=")[1],
        gameView: {},
        shipLocation: [],
        player1: 0,
        player2: 0
    },
    created: function(){
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
            
            for(let i=0;i<2;i++){
                if(data.game.gamePlayers[i].player.id == app.id){
                    app.player1 = data.game.gamePlayers[i].player.email;
                }else{
                    app.player2 = data.game.gamePlayers[i].player.email;
                }
            }
            

            
            console.log(data.ships["0"].type);
            wang = data.ships;
            shipLocations = [];
            console.log(wang);
            
            for(let i=0;i<wang.length;i++){
                shipLocations = data.ships[i].location;
                console.log(shipLocations);
                for(let j=0;j<shipLocations.length;j++){
                     document.getElementById(data.ships[i].location[j]).setAttribute("class", "red");
                }
            }
                  
            
            
            });
        
        
        


    },
    methods: {
        

        }
        
    })
    



