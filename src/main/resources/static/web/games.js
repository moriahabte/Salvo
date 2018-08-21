var app = new Vue({
   el: '#app',
   data: {
       message: 'PLAYERS SCORE BOARD',
       players:"",
       main: {},


   },
   created: function () {

       this.dataServer();

   },

   methods: {

       dataServer: function () {
           fetch("/api/leaderBoard", {
                   method: "GET",
                   creatential: "include",
               })
               .then(function (response) {
                   if (response.ok)
                       return response.json()

               })
               .then(function (json) {

                   main = json;
                   app.main = json;
                   console.log(main);
//                    app.addTable();

               })
               .catch(function (error) {
                   console.log(error);

               })
       },
 
//    addTable: function () {
//        for (var i = 0; i < this.main.length; i++) {
//            var players = this.main[i].player;
//             this.players = players;
//            console.log(players);
//        }
//
//      
//    }
       
   }
})