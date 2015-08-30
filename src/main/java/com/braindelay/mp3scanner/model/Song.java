package com.braindelay.mp3scanner.model;

import org.apache.commons.lang3.math.NumberUtils;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

/**
 * Created by Daniel on 03/08/2015.
 */
public class Song {


    public interface Fields {
        String ARTIST = "artist";
        String ALBUM = "album";
        String TITLE = "title";
    }

    private String title;
    private String artist;
    private String album;
    private String path;
    private String track;

    @Id
    private ObjectId id;

    private Song() {
        // for spring data
    }

    public Song(String artist, String album, String title, String path, String track) {
        this();
        this.artist = artist;
        this.album = album;
        this.title = title;
        this.path = path;
        this.track = track;
    }

    private ObjectId getId() {
        return id;
    }

    public String getSongId() {
        return getId().toHexString();
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public int getTrackNumber(){
        if(NumberUtils.isDigits(track)){
            return NumberUtils.toInt(track);
        }else{
            return 0;
        }

    }

}
