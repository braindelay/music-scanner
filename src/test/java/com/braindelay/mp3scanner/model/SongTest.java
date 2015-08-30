package com.braindelay.mp3scanner.model;


import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Daniel on 29/08/2015.
 */
public class SongTest {
    @Test
    public void testSongType(){
        Song song = new Song("artist","album", "title", "path", "9");
        Assert.assertEquals(9, song.getTrackNumber());

    }
}
