<%--
  Created by IntelliJ IDEA.
  User: vasco
  Date: 16/01/2023
  Time: 12:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Criar Exercicio</title>

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
    <div style="height: 20%">

    </div>
    <div class="container" ng-app="criaExeApp" ng-controller="criaExeController">
        <div class="panel panel-default container col-sm-12">
            <div class="panel-header container col-sm-12">
                <div class="pull-left col-sm-8">
                    <h2>Exercicio</h2>
                </div>
                <div class="pull-right col-sm-2" style="padding-top: 20px">
                    <button class="btn btn-primary btn-success" ng-click="criarExercicio()">Terminar</button>
                </div>
            </div>
            <div class="panel-body container col-sm-12">
                <div class="col-sm-12 container pull-left">
                    <div class="col-sm-12">
                        Nome
                    </div>
                    <input class="col-sm-12" placeholder="Nome do exercicio..." ng-model="exercicio.nome"/>
                </div>
                <div class="col-sm-12 panel panel-body container pull-left" ng-repeat="e in exercicio.etapas">
                    <div class="col-sm-12 container pull-left" style="padding-top: 10px">
                        <div class="col-sm-12" style="padding-bottom: 10px">
                            <h4>Etapa {{$index+1}}</h4>
                        </div>
                        <div class="col-sm-3">
                            Pergunta
                        </div>
                        <input class="col-sm-12" placeholder="Pergunta..." ng-model="e.pergunta"/>
                       <!-- {{e.pergunta}} -->
                        <!--{{e.index}} -->
                    </div>
                </div>
                <div class="col-sm-12 container" style="padding:5%">
                    <button class="btn btn-primary btn-success col-sm-4 col-sm-offset-4" ng-click="addEtapa()">+ Etapa</button>
                </div>
            </div>
        </div>

        <!--{{exercicio.nome}}-->
        <!--{{exercicio.professor}}-->
    </div>
</body>

<script>

    var myApp = angular.module('criaExeApp', ['ngCookies']);
    myApp.controller('criaExeController', function($scope, $http, $window, $cookies) {

        $scope.exercicio = {
            professor : $cookies.getObject("user"),
            nome : "",
            etapas : [{
                index : 0,
                pergunta : ""
            }]
        };

        $scope.addEtapa = function (){
            $scope.exercicio.etapas.push({
                index : $scope.exercicio.etapas[$scope.exercicio.etapas.length-1].index + 1,
                pergunta : ""
            });
        };

        $scope.criarExercicio = function(){
            $http({
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    url: './rs/exercicio/criar',
                    data: $scope.exercicio
                }
            ).then(function (response) {
                console.log(response);

                if(response.data === "T"){
                    alert("Exercicio Registado!");
                    $window.location.href = "menuProfessor.jsp"
                }
                else{
                    alert("Erro! Não foi possivel registar");
                }
            }
            ).catch(function onError(error) {
                console.log(error);
            });;
        };

    });

</script>


</html>
