'use strict';

var momProject = angular.module('momProject', []);

momProject.controller('projectController', function ($scope, $http) {

    $scope.listProjects = {};
    $scope.updateProject = function(){
        $http.get('/api/project/list')
            .then(sukses, gagal);
        function sukses(response) {
            $scope.listProjects = response.data;
        }

        function gagal(response) {
            console.log(response);
            alert('Error : ' + response);
        }
    }

    $scope.updateProject();

});