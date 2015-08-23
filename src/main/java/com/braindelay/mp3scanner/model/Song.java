package com.braindelay.mp3scanner.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

/**
 * Created by Daniel on 03/08/2015.
 */
public class Song  {
    public interface Fields {
        String ARTIST = "artist";
        String ALBUM = "album";
        String TITLE = "title";
    }
    private String title;
    private String artist;
    private String album;
    @Id
    private ObjectId id;

    private Song() {
        // for spring data
    }
    public Song(String artist, String album, String title) {
        this();
        this.artist = artist;
        this.album = album;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }


}
