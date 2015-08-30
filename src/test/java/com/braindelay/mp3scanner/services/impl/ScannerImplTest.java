package com.braindelay.mp3scanner.services.impl;

import com.braindelay.mp3scanner.dao.JobDAO;
import com.braindelay.mp3scanner.dao.SongDAO;
import com.braindelay.mp3scanner.model.JobData;
import com.braindelay.mp3scanner.model.Song;
import com.braindelay.mp3scanner.services.impl.jms.TaskQueueHelper;
import org.bson.types.ObjectId;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Daniel on 23/08/2015.
 */
@RunWith(value = MockitoJUnitRunner.class)
public class ScannerImplTest {

    @Mock
    private JobDAO jobDAO;

    @Mock
    private SongDAO songDAO;
    @Mock
    private TaskQueueHelper taskQueue;

    @InjectMocks
    private ScannerImpl mock = new ScannerImpl() {
        private MP3File audioFile = new MP3File(){
            @Override
            public Tag getTag() {
                if (null == super.getTag()){
                    setTag(new ID3v24Tag());
                }
                return super.getTag();
            }
        };
        @Override
        protected AudioFile loadMP3(JobData job) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
            return audioFile;
        }
    };

    @Test
    public void testProcess() throws IOException, TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException {

        // this finds out where the temp folder is
        File tempFolder = File.createTempFile("123", ".456").getParentFile();

        // th is makes sure we have a test folder
        // and that it's empty
        File testFolder = new File(tempFolder, getClass().getSimpleName());
        FileSystemUtils.deleteRecursively(testFolder);
        testFolder.mkdirs();

        // now, create two test files in here
        File testFile1= new File(testFolder, "aaa.mp3");
        testFile1.createNewFile();
        // this file is in a sub folder
        File testFile2= new File(testFolder, "sub/bbb.mp3");
        testFile2.mkdirs();
        testFile2.createNewFile();

        {
            JobData job = new JobData(JobData.JobType.Task);
            job.setPath(testFolder.getCanonicalPath());
            mock.process(job);

            job.setPath(testFile1.getCanonicalPath());
            mock.loadMP3(null).getTag().addField(FieldKey.ARTIST, "Bowie, David");
            mock.loadMP3(null).getTag().addField(FieldKey.ALBUM, "Station to Station");
            mock.loadMP3(null).getTag().addField(FieldKey.TITLE, "Golden Years");

            mock.process(job);
        }
        // now, pass in a directory, and then a request for a single file,
        // and check that we have created the subtasks as expected
        // and the song
        ArgumentCaptor<JobData> tasks = ArgumentCaptor.forClass(JobData.class);
        verify(taskQueue, times(2)).send(tasks.capture());
        Assert.assertEquals(new File(testFolder, "aaa.mp3").getCanonicalPath(), tasks.getAllValues().get(0).getPath());
        Assert.assertEquals(new File(testFolder, "sub").getCanonicalPath(), tasks.getAllValues().get(1).getPath());

        ArgumentCaptor<Song> songs = ArgumentCaptor.forClass(Song.class);
        verify(songDAO, times(1)).save(songs.capture());
        Song song = songs.getValue();
        Assert.assertEquals("Bowie, David", song.getArtist());
        Assert.assertEquals("Station to Station", song.getAlbum());
        Assert.assertEquals("Golden Years", song.getTitle());

        verify(jobDAO, times(1)).incrementSongCount(any(ObjectId.class), eq(new File(testFolder, "aaa.mp3").getCanonicalPath()));
    }


    @Test
    public void testJobs() {

        mock.getAllJobs();
        verify(jobDAO, times(1)).getUIJobs(JobData.JobState.values());

        mock.getCurrentJobs();
        verify(jobDAO, times(1)).getUIJobs(JobData.JobState.Created, JobData.JobState.Running);



    }

    @Test
    public void startJobs() {
        mock.startJob("path to somewhere");

        ArgumentCaptor<JobData> job = ArgumentCaptor.forClass(JobData.class);
        verify(jobDAO, times(1)).save(job.capture());

        Assert.assertEquals("path to somewhere", job.getValue().getPath());
        Assert.assertEquals(JobData.JobState.Created, job.getValue().getState());

        verify(taskQueue, times(1)).send(job.getValue());
    }

    @Test
    public void cancelJob() {
        JobData job= new JobData(JobData.JobType.UI);
        job.setState(JobData.JobState.Running);
        mock.cancelJob(job);

        verify(jobDAO).save(job);
        Assert.assertEquals(JobData.JobState.Cancelled, job.getState());
    }

    @Test
    public void testGetBtId(){
        ObjectId id = new ObjectId("55e1e946ce8efeee367cc219");
        mock.getJob(id);
        verify(jobDAO, times(1)).find(id);
    }
}
