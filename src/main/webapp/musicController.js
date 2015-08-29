
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



    $scope.getSongs = function() {
          $http.get('/music/library/songs' , { cache : false } ).success(function(data) {
           $scope.songs = data;
          });
        };

  // Store the artist filter
    $scope.setFilter = function(filter) {
      $scope.musicFilter = filter;
   };



 $scope.searchSongs = function(item){
    if(null == item || null == $scope.musicFilter || null == $scope.musicFilter.artist) {
      return true;
    } else {

      return item.artist == $scope.musicFilter.artist
      && (null == $scope.musicFilter.album || item.album ==  $scope.musicFilter.album);
    }
    return true;
 };

  $scope.renderTitle = function(song) {
    return song.trackNumber +  "- " + song.title;
  }
  $scope.play = function(song) {

    $('#playerDiv').show();
    var audio = $("#player");
        $("#mp3_src").attr("src", "/music/download/" + song.songId);
        /****************/
        audio[0].pause();
        audio[0].load();//suspends and restores all audio element
        audio[0].play();
  }

  }]);
