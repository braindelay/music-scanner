package com.braindelay.mp3scanner.services.impl;

import com.braindelay.mp3scanner.dao.SongDAO;
import com.braindelay.mp3scanner.services.MusicDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Daniel on 23/08/2015.
 */
@RunWith(value = MockitoJUnitRunner.class)
public class MusicDatabaseImplTest  {

    @Mock
    SongDAO songDAO;

    @InjectMocks
    private MusicDatabase mock = new MusicDatabaseImpl();

    @Test
    public void testDictionary(){
        mock.getArtists();
        mock.getAlbums("Bowie, David");
        mock.getSongs("Bowie, David", "Station to Station");


        verify(songDAO,times(1)).getArtists();
        verify(songDAO,times(1)).getAlbums("Bowie, David");
        verify(songDAO,times(1)).getSongs("Bowie, David", "Station to Station");
    }

}
