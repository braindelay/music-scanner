package com.braindelay.mp3scanner.ui;

import com.braindelay.mp3scanner.model.JobData;
import com.braindelay.mp3scanner.model.Song;
import com.braindelay.mp3scanner.services.MusicDatabase;
import com.braindelay.mp3scanner.services.Scanner;
import org.apache.catalina.ssi.ByteArrayServletOutputStream;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Daniel on 23/08/2015.
 */
@RunWith(value = MockitoJUnitRunner.class)
public class ScanMusicTest {

    @Mock
    private Scanner scanner;

    @Mock
    private MusicDatabase musicDatabase;

    @InjectMocks
    private ScanMusic mock = new ScanMusic();

    @Test
    public void testDictionary() {
        mock.getArtists();
        verify(musicDatabase).getArtists();

        mock.getAlbums();
        verify(musicDatabase).getAlbums(null);

        mock.getSongs();
        verify(musicDatabase).getSongs(null, null);
    }

    @Test
    public void testJobs() {
        mock.getCurrentJobs();
        verify(scanner).getCurrentJobs();

        ObjectId id = new ObjectId();
        mock.getJobData(id);
        verify(scanner).getJob(id);

        JobData job = new JobData(JobData.JobType.UI);
        job.setPath("a path");
        mock.scanDirectory(job);
        verify(scanner).startJob("a path");

    }

    @Test
    public void testGetFile() throws IOException {

        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        ByteArrayServletOutputStream outputStream = new ByteArrayServletOutputStream();
        when(response.getOutputStream()).thenReturn(outputStream);

        String id = "55e1e946ce8efeee367cc218";
        Song song = new Song("artist","album", "title", "path", "9");

        File tempFile = File.createTempFile("ScanMusicTest",".testGetFile");
        song.setPath(tempFile.getCanonicalPath());
        try (FileWriter fileWriter = new FileWriter(tempFile)) {
            FileCopyUtils.copy("this be the verse", fileWriter);
        }

        //this is just checking that we don't fail out on a null
        mock.getFile(id, response);

        // now, mock that when the file exists, we stream
        // it to the response
        when(musicDatabase.get(new ObjectId(id))).thenReturn(song);
        mock.getFile(id, response);
        Assert.assertEquals("this be the verse", new String(outputStream.toByteArray()));
    }
}
