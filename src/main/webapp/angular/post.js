var app = angular
        .module('postsApp', [])
        .value('postUrl', '../webresources/be.crydust.guestbooktwo.entities.post')
        .controller('PostListCtrl', function($scope, $http, postUrl) {
            $http.get(postUrl).success(function(data) {
                $scope.posts = data;
            });
            $scope.delete = function(post) {
                $scope.posts.splice($scope.posts.indexOf(post), 1);
            };
        });
