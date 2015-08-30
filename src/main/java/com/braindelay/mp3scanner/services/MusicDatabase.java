package com.braindelay.mp3scanner.services;

import com.braindelay.mp3scanner.model.AlbumArtist;
import com.braindelay.mp3scanner.model.Song;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by Daniel on 23/08/2015.
 */
public interface MusicDatabase {
    /**
     * Get all the artists from the database
     * @return
     */
    List<AlbumArtist.Artist> getArtists();

    /**
     * Get the albums for a given artist
     * @param artist If none is applied, then all ambuns are returned
     * @return
     */
    List<AlbumArtist> getAlbums(String artist);

    /**
     * Get all the songs for a given artist/album
     * @param artist
     * @param album
     * @return
     */
    List<Song> getSongs(String artist, String album);

    /**
     * Get the identified song
     * @param songId
     * @return
     */
    Song get(ObjectId songId);
}
