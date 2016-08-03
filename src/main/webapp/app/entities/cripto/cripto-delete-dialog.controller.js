(function() {
    'use strict';

    angular
        .module('powercriptoApp')
        .controller('CriptoDeleteController',CriptoDeleteController);

    CriptoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cripto'];

    function CriptoDeleteController($uibModalInstance, entity, Cripto) {
        var vm = this;

        vm.cripto = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Cripto.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
