app.controller('FamilyCtrl', function($scope, $resource) {
    var Parent = $resource('../webresources/be.crydust.guestbooktwo.entities.parent/:parentId', {parentId: '@id'});
    var Child = $resource('../webresources/be.crydust.guestbooktwo.entities.child/:childId', {childId: '@id'});
    Parent.query(function(parents) {
        $scope.parents = parents;
    });
    Child.query(function(children) {
        $scope.children = children;
    });
});
