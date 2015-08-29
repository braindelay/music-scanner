package com.braindelay.mp3scanner.dao;

import com.braindelay.mp3scanner.model.AlbumArtist;
import com.braindelay.mp3scanner.model.Song;

import java.util.List;

/**
 * Created by Daniel on 03/08/2015.
 */


public interface SongDAO extends MongoDAO<Song> {

    public List<AlbumArtist.Artist> getArtists();
    public List<AlbumArtist> getAlbums(String artist);
    public List<Song> getSongs(String artist, String album);

}
