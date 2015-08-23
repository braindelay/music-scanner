package com.braindelay.mp3scanner.ui;

import com.braindelay.mp3scanner.model.JobData;
import com.braindelay.mp3scanner.services.MusicDatabase;
import com.braindelay.mp3scanner.services.Scanner;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

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
}
