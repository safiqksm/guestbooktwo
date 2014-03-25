app.controller('PostCtrl', function($scope, $resource) {
    var Post = $resource('../webresources/be.crydust.guestbooktwo.entities.post/:postId', {postId: '@id'});
    Post.query(function(posts) {
        $scope.posts = posts;
    });
    $scope.author = "Anonymous";
    $scope.message = "Lorem ipsum expletive dolor sit amet.";
    $scope.word = "expletive";
    $scope.create = function(author, message) {
        Post.save({
            author: author,
            message: message
        }, function(post) {
            $scope.posts.push(post);
        });
    };
    $scope.deleteByWord = function(word) {
        // TODO
    };
    $scope.delete = function(post) {
        Post.delete({postId: post.id}, function() {
            $scope.posts.splice($scope.posts.indexOf(post), 1);
        });
    };
    $scope.update = function() {
        // TODO
    };
});
