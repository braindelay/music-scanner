
scanMusicControllers
  .controller('musicCtrl', ['$scope', '$http', function($scope, $http){

    // get the current jobs from the server
    $scope.getArtists = function() {
      $http.get('/music/library/artists' , { cache : false } ).success(function(data) {
        $('#artistsLoading').show();
        $scope.artists = data;
        $('#artistsLoading').hide();

      });
    };
    // get the current albums from the server
    $scope.getAlbums = function() {
          $http.get('/music/library/albums' , { cache : false } ).success(function(data) {
            $('#albumsLoading').show();
            $scope.albums = data;
            $('#albumsLoading').hide();
          });
        };

    // get the current songs from the server
    $scope.getSongs = function() {
          $http.get('/music/library/songs' , { cache : false } ).success(function(data) {
            $('#songsLoading').show();
           $scope.songs = data;
           $('#songsLoading').hide();
          });
        };

  // Store the artist filter
    $scope.setFilter = function(filter) {
      $scope.musicFilter = filter;
   };



  // the function used to filter the songs
  // returns true if the artist/album set by "setFilter" matches the item
  // if the album in setFilter is null, then the album part of the filter isn't applied
   $scope.searchSongs = function(item){
    if(null == item || null == $scope.musicFilter || null == $scope.musicFilter.artist) {
      return true;
    } else {
      return item.artist == $scope.musicFilter.artist
        && (null == $scope.musicFilter.album || item.album ==  $scope.musicFilter.album);
    }
    return true;
  };

  // the function used to render the song titles - returns the title string
  $scope.renderTitle = function(song) {
    return song.trackNumber +  "- " + song.title;
  }

  // play the identified song
  $scope.play = function(song) {

    // show the player
    $('#playerDiv').show();

    // add some track data
    $scope.songAudioData = song;


    //find the player, set the path to the song
    // and then stop, reload, and play it
    var audio = $("#player");
    $("#mp3_src").attr("src", "/music/download/" + song.songId);
    /****************/
    audio[0].pause();
    audio[0].load();//suspends and restores all audio element
    audio[0].play();
  }

  }]);
