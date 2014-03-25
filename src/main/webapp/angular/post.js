angular
        .module('postsApp', [])
        .value('postUrl', '../webresources/be.crydust.guestbooktwo.entities.post')
        .controller('PostListCtrl', function($scope, $http, postUrl) {
            $http
                    .get(postUrl)
                    .success(function(data) {
                        $scope.posts = data;
                    });
        });
