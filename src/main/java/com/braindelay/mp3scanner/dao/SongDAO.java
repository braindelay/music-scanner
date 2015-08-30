package com.braindelay.mp3scanner.dao;

import com.braindelay.mp3scanner.model.AlbumArtist;
import com.braindelay.mp3scanner.model.Song;

import java.util.List;

/**
 * Created by Daniel on 03/08/2015.
 */


public interface SongDAO extends MongoDAO<Song> {

    /**
     * Get all the artists in the database - sorted alphabetically
     * @return
     */
    public List<AlbumArtist.Artist> getArtists();

    /**
     * Get all the albums in the database, sorted by artist and then album.
     * @param artist
     * @return
     */
    public List<AlbumArtist> getAlbums(String artist);

    /**
     * Get all the songs in the database
     * @param artist
     * @param album
     * @return
     */
    public List<Song> getSongs(String artist, String album);

}
