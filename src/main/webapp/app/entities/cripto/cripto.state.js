(function() {
    'use strict';

    angular
        .module('powercriptoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cripto', {
            parent: 'entity',
            url: '/cripto?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Criptos'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cripto/criptos.html',
                    controller: 'CriptoController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('cripto-detail', {
            parent: 'entity',
            url: '/cripto/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Cripto'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cripto/cripto-detail.html',
                    controller: 'CriptoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Cripto', function($stateParams, Cripto) {
                    return Cripto.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cripto',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cripto-detail.edit', {
            parent: 'cripto-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cripto/cripto-dialog.html',
                    controller: 'CriptoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cripto', function(Cripto) {
                            return Cripto.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cripto.new', {
            parent: 'cripto',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cripto/cripto-dialog.html',
                    controller: 'CriptoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                quantidadeHashes: null,
                                tempo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cripto', null, { reload: true });
                }, function() {
                    $state.go('cripto');
                });
            }]
        })
        .state('cripto.edit', {
            parent: 'cripto',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cripto/cripto-dialog.html',
                    controller: 'CriptoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cripto', function(Cripto) {
                            return Cripto.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cripto', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cripto.delete', {
            parent: 'cripto',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cripto/cripto-delete-dialog.html',
                    controller: 'CriptoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Cripto', function(Cripto) {
                            return Cripto.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cripto', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
