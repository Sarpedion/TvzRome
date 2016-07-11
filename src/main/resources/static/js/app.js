angular.module('app', ['ngRoute', 'ngMaterial', 'md.data.table', 'ngMdIcons', 'ngMessages', 'auth', 'home', 'navigation', 'admin', 'profile', 'ngCookies', 'employees', 'newEvidence', 'myEvidences','dashboard', 'ngMessages', 'ngSanitize', 'ngCsv','chart.js']).
    config(function ($routeProvider) {

        $routeProvider.when('/', {
            templateUrl: '/partials/home.html',
            controller: 'home',
            controllerAs: 'controller'
        }).when('/login', {
            templateUrl: '/partials/login.html',
            controller: 'navigation',
            controllerAs: 'controller'
        }).when('/home', {
            templateUrl: '/partials/home.html',
            controller: 'home',
            controllerAs: 'controller'
        }).when('/admin', {
            templateUrl: '/partials/admin.html',
            controller: 'admin',
            controllerAs: 'controller',
            resolve: {
                'auth': function (AuthService) {
                    return AuthService.authorize('ADMIN');
                }
            }
        }).when('/profile', {
            templateUrl: '/partials/profile.html',
            controller: 'profile',
            controllerAs: 'controller'
        }).when('/employees', {
            templateUrl: '/partials/employees.html',
            controller: 'employees',
            controllerAs: 'controller'
        }).when('/evidence', {
            templateUrl: '/partials/newevidence.html',
            controller: 'newEvidence',
            controllerAs: 'controller'
        }).when('/myEvidence', {
            templateUrl: '/partials/myevidence.html',
            controller: 'myEvidences',
            controllerAs: 'controller'
        }).when('/dashboard', {
            templateUrl: '/partials/dashboard.html',
            controller: 'dashboard',
            controllerAs: 'controller'
        }).otherwise('/');

    }).config(function ($mdDateLocaleProvider) {

        $mdDateLocaleProvider.formatDate = function (date) {
             return moment(date).format('DD.MM.YYYY');
        };

        $mdDateLocaleProvider.parseDate = function(dateString) {
            var m = moment(dateString, 'DD.MM.YYYY', true);
            return m.isValid() ? m.toDate() : new Date(NaN);
        };

    }).config(['$mdThemingProvider', function ($mdThemingProvider) {
        'use strict';

        $mdThemingProvider.theme('default')
            .primaryPalette('cyan',
            {})
            .warnPalette('red', {
                'default': '900'
            });
    }]).run(function (auth) {

        auth.init();

    }).factory('AuthService', ['$location', '$http', '$q', '$rootScope', function ($location, $http, $q, $rootScope) {
        return {
            authorize: function (allowedAuthorization) {

                if ($rootScope.authority === allowedAuthorization) {
                    return true;
                } else {
                    return $q.reject('Not Authenticated');
                }

            }
        }
    }]);
