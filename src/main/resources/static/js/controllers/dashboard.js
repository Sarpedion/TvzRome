angular.module('dashboard', []).controller('dashboard', function($http, $scope) {
	var self = this;
	$scope.sharedData.title = 'Dashboard';

    $http.get('rest/dashboard/byHour').then(function (response) {
        var ctx = document.getElementById('byHour').getContext('2d');
        new Chart(ctx).Bar(response.data);          
    });
    
    $http.get('rest/dashboard/presence').then(function (response) {
        var ctx2 = document.getElementById('total').getContext('2d');
        new Chart(ctx2).Pie(response.data);          
    });
    
    

});

