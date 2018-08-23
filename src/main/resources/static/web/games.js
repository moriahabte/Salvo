var app = new Vue({
    el: '#app',
    data: {
        message: 'PLAYERS SCORE BOARD',
        players: "",
        main: {},
        alertDiv: false,
        logged: false,
    },
    created: function () {

        this.dataServer();
        this.getGames();

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

        logIn: function () {
            let user = document.getElementById("inputEmail").value;
            let password = document.getElementById("inputPassword").value;
            fetch("/api/login", {
                    credentials: 'include',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: 'user=' + user + '&password=' + password,
                })
                .then(r => {
                    if (r.status == 200) {
                        this.alertDiv = false;
                        this.logged = true;
                    } else {
                        this.alertDiv = true;
                        this.logged = false;
                    }
                    console.log(r)
                })
                .catch(r => console.log(r))
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
                .catch(r => console.log(r))

        },
        getGames: function () {
           let url = '/api/games';
           fetch(url, {
                   method: "GET",
                   credentials: "include",
               })
               .then((response) => response.json())
               .then(function (data) {
                   console.log(data)
                   if (data.player) {
                       app.logged = true;
                   } else {
                       app.logged = false;
                   }

               })
    },
        signUp: function(){
            let user = document.getElementById("inputEmail").value;
            let password = document.getElementById("inputPassword").value;
            fetch("/api/players", {
                    credentials: 'include',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: 'user=' + user + '&password=' + password,
                })
                .then(r => {
                    if (r.status == 201) {
                        this.logged = true;
                        app.logIn();
                    } else {
                        this.alertDiv = true;
                    }
                    console.log(r)
                })
                .catch(r => console.log(r))
        },
}})
