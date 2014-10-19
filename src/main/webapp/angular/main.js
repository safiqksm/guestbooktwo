window.name = "NG_DEFER_BOOTSTRAP!";
require(['angular', './app', './FamilyCtrl', './PostCtrl'], function (angular) {
    angular.element().ready(function () {
        angular.resumeBootstrap();
    });
});