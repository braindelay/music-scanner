package com.braindelay.mp3scanner.model;

import org.springframework.data.annotation.Id;

/**
 * Created by Daniel on 10/08/2015.
 */
public class AlbumArtist {



    public static class Artist extends AlbumArtist{
        @Id
        private String id;

        public String getArtist() {
            return id ;
        }

    }
    private String artist;
    private String album;

    public String getArtist() {
        return artist ;
    }


    public String getAlbum() {
        return album;
    }


}
