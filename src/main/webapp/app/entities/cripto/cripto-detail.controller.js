(function() {
    'use strict';

    angular
        .module('powercriptoApp')
        .controller('CriptoDetailController', CriptoDetailController);

    CriptoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Cripto'];

    function CriptoDetailController($scope, $rootScope, $stateParams, previousState, entity, Cripto) {
        var vm = this;

        vm.cripto = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('powercriptoApp:criptoUpdate', function(event, result) {
            vm.cripto = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
