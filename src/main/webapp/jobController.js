
scanMusicControllers
  .controller('jobCtrl', ['$scope', '$http', function($scope, $http){

    // get the current jobs from the server
    $scope.getCurrentJobs = function() {
      console.log("getting current jobs");
      $http.get('/scan?v=' +new Date().getTime() , { cache : false } ).success(function(data) {
        console.log("got current jobs: " + data);
        $scope.currentJobs = data;
      });
    };

    // submit jobs
    $scope.submitJob = function() {
      $http.post('/scan/directory',  $scope.job).success(function() {
      $scope.job.path = '';
      $scope.getCurrentJobs();
      });
    };

  }]);
