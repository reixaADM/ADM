<%--
  Created by IntelliJ IDEA.
  User: Luís Reixa
  Date: 07/02/2023
  Time: 15:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Fazer Exercicio</title>

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
<div class="container" ng-app="fazerExeApp" ng-controller="fazerExeController">
    <div style="height: 20%">

    </div>
    <div class="panel panel-default container">
        <div class="panel-header container">
            <h2>Aluno {{user}}</h2>
        </div>
        <div class="panel-body container col-sm-12">
            <div class="col-sm-10">
                <h3>{{nomeExe}}</h3>
            </div>
            <div class="col-sm-2">
                <button ng-click="chamaDocente()">
                    CHAMAR DOCENTE
                </button>
            </div>

            <div class = "panel panel-default col-sm-12">
                <div class="panel-header col-sm-offset-0 col-sm-14" style="padding-top:2%; height:8%">
                    <div class="panel-body container col-sm-12">
                        <!--{{etapas}}-->
                        <div ng-repeat="e in etapas.etapas" style="height: 5%;padding: 2%" class="col-sm-12">
                            <div class="col-sm-14">
                                <div class="col-sm-8">
                                    {{e.pergunta}}
                                </div>
                                <div class="col-sm-4 form-check pull-right">
                                    <input type="checkbox" class="form-check-input pull-right" id="exampleCheck1" ng-model="e.check">
                                </div>
                                </div>
                            </div>
                            <!--<div style="border-bottom: 1px solid rgba(0, 0, 0, .2);height: 150%" class="col-sm-14">
                            </div>-->
                        </div>

                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
</body>
<script>
    var myApp = angular.module('fazerExeApp', ['ngCookies']);
    myApp.controller('fazerExeController', function($scope, $interval, $http, $window, $cookies) {

        $scope.user = $cookies.getObject("user");
        $scope.idAluno = $cookies.getObject("idAluno");
        $scope.idExe = $cookies.getObject("idExe");
        $scope.nomeExe = $cookies.getObject("nomeExe");

        $scope.etapas = {};


        $scope.callInformacaoInicial = function (){

            $http({
                method: 'POST',
                url: './rs/exercicio/exercicio_aluno',
                data: {
                    idAluno : $scope.idAluno,
                    idExe : $scope.idExe
                }
            }).then(function (response) {
                console.log(response.data);

                $scope.etapas = response.data;

                $interval($scope.updateEtapaCheck, 1000);
            })


        }

        $scope.updateEtapaCheck = function (){

            $http({
                method: 'POST',
                url: './rs/exercicio/update_etapacheck',
                data: {
                    idAluno : $scope.idAluno,
                    idExercicio : $scope.idExe,
                    etapas : $scope.etapas.etapas
                }
            }).then(function (response){
                //alert(response.data);

            });
        }

        $scope.callInformacaoInicial();

        $scope.chamaDocente = function (){
            $http({
                method: 'POST',
                url: './rs/exercicio/update_chamadocente',
                data: {
                    idAluno : $scope.idAluno,
                    idExercicio : $scope.idExe
                }
            }).then(function (response){

            });
        }
    });
</script>
</html>
