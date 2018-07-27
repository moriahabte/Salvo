var app = new Vue({
    el: '#app',
    data: {
        message: 'Hello Vue!',
        id: location.search.split("=")[1],
        gameView: {},
        shipLocation:""
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
                
            document.getElementById(data.ships["0"].location["0"]).setAttribute("class", "red");
//                shiplocation.setAttribute("class", "red");
            });
        
        
        


    },
    methods: {
        

        }
        
    })
    



