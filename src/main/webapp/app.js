var scanMusicApp = angular.module('scanMusicApp', [
  'ngRoute',
  'scanMusicControllers',
  'angularGrid'
]);

var scanMusicControllers = angular.module('scanMusicControllers', []);

scanMusicApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/jobs', {
        templateUrl: 'jobs.html',
        controller: 'jobCtrl'
      }).
      when('/music', {
              templateUrl: 'music.html',
              controller: 'jobCtrl'
            }).
      otherwise({
        redirectTo: '/music'
      });
  }]);