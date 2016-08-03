(function() {
    'use strict';
    angular
        .module('powercriptoApp')
        .factory('Cripto', Cripto);

    Cripto.$inject = ['$resource'];

    function Cripto ($resource) {
        var resourceUrl =  'api/criptos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
