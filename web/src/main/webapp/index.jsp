<%--
  Created by IntelliJ IDEA.
  User: vasco
  Date: 09/01/2023
  Time: 11:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <script src="angular.js"></script>
    <script src="angularjsCookies.js"></script>

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">

    <!-- jQuery library -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>

    <!-- Latest compiled JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

</head>
<body>
    <div class="container" ng-app="loginApp" ng-controller="loginController">
        <div style="height: 20%">

        </div>
        <div class="col-sm-offset-2 col-sm-8">
            <div class="panel-header">
                <h1>Login</h1>
            </div>
            <div class="panel panel-default">
                <div class="panel-body">
                    <div style="align-content: center; padding: 2px">
                        <div class="col-sm-12">
                            <p>Username</p>
                            <input class="col-sm-12" placeholder="Email/Nome..." ng-model="login.username"/>
                        </div>
                        <div class="col-sm-12" style="padding-top: 30px">
                            <p>Password</p>
                            <input type="password" placeholder="Password..." class="col-sm-12" ng-model="login.password"/>
                        </div>
                    </div>
                    <div class="col-sm-12" style="padding-top: 10px">
                        <button type="btn btn-primary btn-success col-sm-offset-4 col-sm-2" ng-click="buttonLogin()" class="button btn-primary btn-success col-sm-2 col-sm-offset-5">LOGIN</button>
                    </div>
                </div>
            </div>
            <!--
            <div class="panel-header">
                <h1>Debugger</h1>
            </div>
            <div class="panel panel-default">
                <div class="panel-body">
                    {{login.username}}
                </div>
            </div>
            -->
        </div>
    </div>


    <script>
        var myApp = angular.module('loginApp', ['ngCookies']);
        myApp.controller('loginController', function($scope, $http, $window, $cookies){

            $scope.login = {
                username : "",
                password : ""
            };

            $scope.buttonLogin = function(){
                $http({
                    method: 'POST',
                    url: './rs/log/login',
                    data: {
                        user: $scope.login.username,
                        password : $scope.login.password
                    }
                    }
                ).then(function (response) {
                    if(response.data === 'A'){
                        $cookies.putObject("user", $scope.login.username);
                        $cookies.putObject("password", $scope.login.password);
                        $window.location.href = "menuAluno.jsp";
                    }
                    else if(response.data === 'P'){
                        $cookies.putObject("user", $scope.login.username);
                        $cookies.putObject("password", $scope.login.password);
                        alert($cookies.getObject("user"));
                        $window.location.href = "menuProfessor.jsp";
                    }
                    else{
                        alert("User/email ou password errada");
                    }
                });
            };

        });

    </script>


</body>
</html>
