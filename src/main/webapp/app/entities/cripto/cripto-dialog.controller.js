(function() {
    'use strict';

    angular
        .module('powercriptoApp')
        .controller('CriptoDialogController', CriptoDialogController);

    CriptoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cripto'];

    function CriptoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cripto) {
        var vm = this;

        vm.cripto = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cripto.id !== null) {
                Cripto.update(vm.cripto, onSaveSuccess, onSaveError);
            } else {
                Cripto.save(vm.cripto, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('powercriptoApp:criptoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
