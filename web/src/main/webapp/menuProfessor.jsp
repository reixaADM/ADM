<%--
  Created by IntelliJ IDEA.
  User: vasco
  Date: 12/01/2023
  Time: 15:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>PROFESSOR</title>
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
<div class="container" ng-app="professorApp" ng-controller="professorController">
    <div style="height: 20%">

    </div>
    <div class="panel panel-default container">
        <div class="panel-header container">
            <h2>Prof {{user}}</h2>
        </div>
        <div class="panel-body container col-sm-12">
            <div class = "panel panel-default col-sm-12">
                <div class="panel-header col-sm-offset-0 col-sm-14" style="padding-top:2%; height:8%">
                    <div class="col-sm-offset-0 col-sm-10">
                        <h4>Exercicios</h4>
                    </div>
                    <div class="col-sm-2" style="padding:0%">
                        <button class="btn btn-primary btn-success col-sm-offset-1 pull-right" ng-click="criarExercicio()">+</button>
                    </div>
                </div>

                <div class="panel-body container col-sm-12">
                    <div>
                        <div>
                            <div class="col-sm-12">
                                ATIVO
                            </div>
                            <div class="col-sm-12">
                                <div ng-repeat="e in listExs" ng-if="!e.terminaExercicio">
                                    <div class="col-sm-10">
                                        {{e.nome}}
                                    </div>

                                    <div class="col-sm-2" style="padding:0%">
                                        <button class="pull-right" ng-click="verExercicio(e.id)">
                                            <span class="glyphicon glyphicon-triangle-right" ></span>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div>
                            <div class="col-sm-12">
                                TERMINADO
                            </div>
                            <div class="col-sm-12">
                                <div ng-repeat="e in listExs" ng-if="e.terminaExercicio">
                                    <div class="col-sm-10">
                                        {{e.nome}}
                                    </div>

                                    <div class="col-sm-2" style="padding:0%">
                                        <button class="pull-right" ng-click="verExercicio(e.id)" >
                                            <span class="glyphicon glyphicon-triangle-right "></span>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>



                    </div>

                </div>
            </div>
        </div>
    </div>
</div>
</body>

<script>

    var myApp = angular.module('professorApp', ['ngCookies']);
    myApp.controller('professorController', function($scope, $http, $window, $cookies) {

        $scope.user = $cookies.getObject("user");

        $scope.listExs = [];

        $scope.loadExs = function(){
            $http({
                    method: 'POST',
                    url: './rs/professor/listExByProf',
                    data: {
                        user: $scope.user,
                    }
                }
            ).then(function (response) {

                console.log(response.data);
                $scope.listExs = response.data;
            });
        };

        $scope.criarExercicio = function (){
            $window.location.href = "criaExercicio.jsp"
        };

        $scope.verExercicio = function (id){
            $cookies.putObject("exercicio", id);
            $window.location.href = "verExercicio.jsp"
        };

        $scope.loadExs();
    });

</script>
</html>

