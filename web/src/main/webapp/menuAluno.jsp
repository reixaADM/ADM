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
    <title>ALUNO</title>

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
<div class="container" ng-app="alunoApp" ng-controller="alunoController">
    <div style="height: 20%">

    </div>
    <div class="panel panel-default container">
        <div class="panel-header container">
            <h2>Aluno {{user}}</h2>
        </div>
        <div class="panel-body container col-sm-12">
            <div class = "panel panel-default col-sm-12">
                <div class="panel-header col-sm-offset-0 col-sm-14" style="padding-top:2%; height:8%">
                    <div class="panel-body container col-sm-12">
                        <div ng-repeat="e in exercicios.exercicios" ng-if="!e.terminaExercicio" style="height: 5%;padding: 2%" class="col-sm-12">
                            <div class="col-sm-14">
                                <div class="col-sm-4">
                                    {{e.nomeExercicio}}
                                </div>
                                <div class="col-sm-4">
                                    {{e.nomeUc}}
                                </div>
                                <div class="btn btn-secondary pull-right" style="padding:0%">
                                    <button ng-click="entrarExercicio(e)">
                                        <span class="glyphicon glyphicon-triangle-right" ></span>
                                    </button>
                                </div>
                            </div>
                            <div style="border-bottom: 1px solid rgba(0, 0, 0, .2);height: 150%" class="col-sm-14">
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
    var myApp = angular.module('alunoApp', ['ngCookies']);
    myApp.controller('alunoController', function($scope, $http, $window, $cookies) {

        $scope.user = $cookies.getObject("user");

        $scope.aluno = {
            nome : $cookies.getObject("user")
        };

        $scope.exercicios = {};

        $scope.callExercicioAluno = function (){
            $http({
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                url: './rs/exercicio/apresentar',
                data: $scope.aluno
            }).then(function (response) {
                    console.log(response);

                    $scope.exercicios = response.data;

                }
            )
        };
        
        $scope.entrarExercicio = function (exercicio){
            $cookies.putObject("idAluno", $scope.exercicios.idAluno);
            $cookies.putObject("idExe", exercicio.idExercicio);
            $cookies.putObject("nomeExe", exercicio.nomeExercicio);
            $window.location.href = "fazerExercicio.jsp";
        }

        $scope.callExercicioAluno();

    });
</script>
</html>
