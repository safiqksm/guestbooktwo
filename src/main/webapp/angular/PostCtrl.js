app.controller('PostCtrl', function($scope, $resource, $filter) {
    var Post = $resource(
            '../rest/be.crydust.guestbooktwo.entities.post/:postId',
            {postId: '@id'}, {update: {method: 'PUT'}});
    $scope.messages = [];
    function addMessage(messageText) {
        var nowEpoch = (new Date()).getTime();
        var maxAge = 5 * 1000;
        $scope.messages = $scope.messages.filter(function(message) {
            return (nowEpoch - message.ctime) < maxAge;
        });
        $scope.messages.push({
            ctime: nowEpoch,
            text: messageText
        });
    }
    Post.query(function(posts) {
        $scope.posts = posts;
    }, function(httpResponse) {
        addMessage('error ' + httpResponse.status + ': querying Posts failed');
    });
    $scope.author = "Anonymous";
    $scope.message = "Lorem ipsum expletive dolor sit amet.";
    $scope.word = "expletive";
    $scope.updating = false;
    $scope.currentPostBackup = null;
    $scope.currentPost = null;
    $scope.create = function() {
        Post.save({
            author: $scope.author,
            message: $scope.message
        }, function(post) {
            $scope.posts.push(post);
//            addMessage('Post created successfully');
        }, function(httpResponse) {
            addMessage('error ' + httpResponse.status + ': creating Post failed');
        });
    };
    $scope.deleteByWord = function() {
        var word = $scope.word;
        if (word.length > 0) {
            $scope.posts = $scope.posts.filter(function(post){
                if (post.message.indexOf(word) === -1) {
                    return true;
                } else {
                    Post.delete({postId: post.id});
                    return false;
                }
            })
        } else {
            addMessage('error: word shouldn\'t be empty');
        }
    };
    $scope.delete = function(post) {
        Post.delete({postId: post.id}, function() {
            $scope.posts.splice($scope.posts.indexOf(post), 1);
//            addMessage('Post deleted successfully');
        }, function(httpResponse) {
            addMessage('error ' + httpResponse.status + ': deleting Post failed');
        });
    };
    $scope.beginUpdate = function(post) {
        $scope.currentPost = post;
        $scope.currentPostBackup = angular.extend({}, post);
        $scope.updating = true;
    };
    $scope.endUpdate = function() {
        Post.update($scope.currentPost, function() {
            $scope.updating = false;
            $scope.currentPost = null;
            $scope.currentPostBackup = null;
            addMessage('Post updated successfully');
        }, function(httpResponse) {
            addMessage('error ' + httpResponse.status + ': updating Post failed');
        });
    };
    $scope.cancelUpdate = function() {
        $scope.posts[$scope.posts.indexOf($scope.currentPost)] = $scope.currentPostBackup;
        $scope.updating = false;
        $scope.currentPost = null;
        $scope.currentPostBackup = null;
//        addMessage('Post update cancelled');
    };
});
