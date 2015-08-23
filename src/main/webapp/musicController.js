
scanMusicControllers
  .controller('musicCtrl', ['$scope', '$http', function($scope, $http){

    // get the current jobs from the server
    $scope.getArtists = function() {
      $http.get('/music/library/artists' , { cache : false } ).success(function(data) {
        $scope.artists = data;
      });
    };

    $scope.getAlbums = function() {
          $http.get('/music/library/albums' , { cache : false } ).success(function(data) {
            $scope.albums = data;
          });
        };


    $scope.gridOptions = {
            enableColResize: true,
            virtualPaging: true, // this is important, if not set, normal paging will be done
            rowSelection: 'single',
            rowDeselection: true,
            columnDefs: [
              {headerName: "Song", field: "title", width: 100}
            ]
        };


    $scope.getSongs = function() {
          $http.get('/music/library/songs' , { cache : false } ).success(function(data) {
           $scope.songs = data;
/*
            var allOfTheData = data;
            var dataSource = {
                rowCount: null, // behave as infinite scroll
                pageSize: 100,
                overflowSize: 100,
                maxConcurrentRequests: 2,
                maxPagesInCache: 2,
                getRows: function (params) {
                    console.log('asking for ' + params.startRow + ' to ' + params.endRow);
                    // At this point in your , you would call the server, using $http if in AngularJS.
                    // To make the demo look recodeal, wait for 500ms before returning
                    setTimeout( function() {
                        // take a slice of the total rows
                        var rowsThisPage = allOfTheData.slice(params.startRow, params.endRow);
                        // if on or after the last page, work out the last row.
                        var lastRow = -1;
                        if (allOfTheData.length <= params.endRow) {
                            lastRow = allOfTheData.length;
                        }
                        // call the success callback
                        params.successCallback(rowsThisPage, lastRow);
                    }, 500);
                }
            };

            $scope.gridOptions.api.setDatasource(dataSource);
*/
          });
        };

  // Store the artist filter
    $scope.setFilter = function(filter) {
      $scope.musicFilter = filter;

      console.log('Setting filter: ' + $scope.musicFilter.artist + ';' + $scope.musicFilter.album);
    };

    $scope.getFilterAlbum = function() {
      used = (null != $scope.musicFilter && null != $scope.musicFilter.album) ? album : '';
      console.log('used: ' + used);
      return used;
    }



  }]);
