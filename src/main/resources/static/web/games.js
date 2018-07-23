var app = new Vue({
    el: '#app',
    data: {
        message: 'Hello Vue!',
        allGames: {}

    },
    created: function () {
        fetch("/api/games", {
                method: "GET",
                credentials: "include",
            })

            .then(function (response) {
                return response.json();
            })
            .then(function (myJson) {
                console.log(myJson);
                data = myJson;
                app.allGames = data;
            });


    },
//    methods: {
//        getGames : function () {
//            let games = this.allGames;
//            let array = [];
//            for (let i = 0;i<games.length; i++){
//                let object = {
//                    created: new Date(games[i].created).toLocaleString(),
//                    id: games[i].id,
//                    pol: "pol"
////                    gamePlayers: [
////                        {player: games[i].gamePlayers[0].player.email},
////                    ],
//                }
//                
//                array.push(object);
//            }
//            this.games = array;
//        
//        }
//    }

})
