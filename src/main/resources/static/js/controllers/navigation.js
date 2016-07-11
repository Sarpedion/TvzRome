angular.module('navigation', ['ngRoute', 'auth'])
    .controller(
    'navigation', ['$scope', '$route', 'auth', '$timeout', '$mdSidenav', '$location', '$mdDialog',

        function ($scope, $route, auth, $timeout, $mdSidenav, $location, $mdDialog) {

            var self = this;

            var errorMsg = null;

            var originatorEv;

            self.openMenu = function ($mdOpenMenu, ev) {
                originatorEv = ev;
                $mdOpenMenu(ev);
            };

            $scope.sharedData = {};
            $scope.sharedData.title = null;
            $scope.sharedData.authority = null;

            self.credentials = {};

            self.authenticated = function () {
                return auth.authenticated;
            };

            $scope.toggleLeft = buildDelayedToggler('left');

            self.navHome = function navHome() {
                $location.path('/home');
            };

            self.navAdmin = function navAdmin() {
                $location.path('/admin');
            };

            self.navProfile = function navProfile() {
                $location.path('/profile');
            };

            self.navEmployees = function navEmployees() {
                $location.path('/employees');
            };

            self.navEvidence = function navEvidence() {
                $location.path('/evidence');
            };

            self.navMyEvidence = function navMyEvidence() {
                $location.path('/myEvidence');
            };
            
            self.navDashboard = function navDashboard() {
                $location.path('/dashboard');
            };

            function buildDelayedToggler(navID) {
                return debounce(function () {
                    $mdSidenav(navID)
                        .toggle();
                }, 200);
            }

            function debounce(func, wait, context) {
                var timer;

                return function debounced() {
                    var context = $scope,
                        args = Array.prototype.slice.call(arguments);
                    $timeout.cancel(timer);
                    timer = $timeout(function () {
                        timer = undefined;
                        func.apply(context, args);
                    }, wait || 10);
                };
            }

            $scope.close = function () {
                $mdSidenav('left').close();
            };

            self.login = function () {

                auth.authenticate(self.credentials, function (authenticated) {
                    if (authenticated) {
                        console.log("Login succeeded");
                        self.error = false;
                        errorMsg = null;
                    } else {
                        console.log("Login failed");
                        self.error = true;
                        self.errorMsg = 'Prijava neuspješna!';
                    }
                });

            };

            self.logout = auth.clear;

            self.changePassword = function () {

                $mdDialog.show({
                    controller: ChangePasswordController,
                    templateUrl: 'partials/dialogs/changePassword.html',
                    parent: angular.element(document.body),
                    clickOutsideToClose: false
                }).then(function () {
                    $location.path('/home');
                })
            };

        }]);

function ChangePasswordController($scope, $mdDialog, $http) {


    $scope.changePasswordRequest = {
        oldPassword : undefined,
        newPassword : undefined,
        newPasswordConfirm : undefined
    };

    $scope.changePasswordErrorMsg = null;

    $scope.hideDialog = function () {
        $mdDialog.hide();
    };

    $scope.changePassword = function () {

        $http.post('/rest/employee/password', $scope.changePasswordRequest).success(function (data) {
            $mdDialog.hide();
        })
            .error(function (data, status) {
                $scope.changePasswordErrorMsg = 'Failed to change password.';
            });

    };
}

