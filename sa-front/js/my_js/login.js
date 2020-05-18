/*登录中的js*/
/*$().ready(function(){
    /!*登录*!/
    $("#login-btn").click(function () {
        var username = $("#username").val();
        var password = $("#password").val();
        console.log("0000000000" + username + password);

        var user = new Object();
        user.username = this.username;
        user.password = this.password;
        $.post("http://localhost:8080/stary_animals/login",{user: user, type : 2},function(result){
            alert(result);
        });

    });
});*/


/*使用vue登录*/
