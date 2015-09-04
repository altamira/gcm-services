/* global angular,hljs */
(function () {
    var GCMRegistration = angular.module('GCMRegistration', ['ngRoute']);

    GCMRegistration.service('RegistrationService', function ($http, $routeParams) {
    	$http.defaults.headers.common['Content-Type'] = 'application/json';
        $http.defaults.headers.common['Authorization'] = 'Basic ' + 'dXNlcjpLeHNAM2Ft'; /*Base64.encode('user' + ':' + 'password');*/
        
        this.validate = function () {
            return $http.get('http://gcm.altamira.com.br/api/0.0.4-SNAPSHOT/register/validate/' + $routeParams.token);
        };
    });

    GCMRegistration.controller('RegistrationCtrl', function (RegistrationService, $scope) {
        var self = this;
        
        $scope.valid = false;

        self.validate = function () {
        	RegistrationService.validate().then(function(sucess) {
        		$scope.valid = true;
        		alert("Seu email foi validado com sucesso, o aplicativo esta pronto para receber notificações do Sistema de Vendas da Altamira.");
        	}, function(error) {
        		alert("Erro ao validar o token.");
        	});
        };

    });
    
    GCMRegistration.config(function ($routeProvider) {
        $routeProvider.when('/token/:token', {templateUrl: '/pages/main.tpl.html'});
        $routeProvider.otherwise({redirectTo: '/token'});
    });
}());