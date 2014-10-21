window.name = "NG_DEFER_BOOTSTRAP!";
require(['angular', './app', './FamilyCtrl', './PostCtrl'/*, 'requirejs-domready'*/], function (angular) {
    angular.resumeBootstrap();
});