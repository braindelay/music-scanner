package com.braindelay.mp3scanner.model;

import org.springframework.data.annotation.Id;

/**
 * Created by Daniel on 10/08/2015.
 */
public class AlbumArtist {


    /**
     * The album artist model
     */
    public static class Artist extends AlbumArtist{
        @Id
        private String id;

        public String getArtist() {
            return id ;
        }

    }
    private String artist;
    private String album;

    /**
     * The artist for the album
     * @return
     */
    public String getArtist() {
        return artist ;
    }


    /**
     * Get the album title
     * @return
     */
    public String getAlbum() {
        return album;
    }


}
