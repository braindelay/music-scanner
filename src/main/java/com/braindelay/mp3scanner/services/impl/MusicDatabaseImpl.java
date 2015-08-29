package com.braindelay.mp3scanner.services.impl;

import com.braindelay.mp3scanner.dao.SongDAO;
import com.braindelay.mp3scanner.model.AlbumArtist;
import com.braindelay.mp3scanner.model.Song;
import com.braindelay.mp3scanner.services.MusicDatabase;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Daniel on 23/08/2015.
 */
@Service
public class MusicDatabaseImpl implements MusicDatabase {

    @Autowired
    private SongDAO songDAO;


    /**
     * {@inheritDoc}
     */
    @Override
    public List<AlbumArtist.Artist> getArtists() {
        return songDAO.getArtists();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AlbumArtist> getAlbums(String artist) {
        return songDAO.getAlbums(artist);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Song> getSongs(String artist, String album) {
        return songDAO.getSongs(artist,album);
    }

    @Override
    public Song get(ObjectId songId) {
        return songDAO.find(songId);
    }

}
