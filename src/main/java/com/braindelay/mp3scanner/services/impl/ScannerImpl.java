package com.braindelay.mp3scanner.services.impl;

import com.braindelay.mp3scanner.dao.JobDAO;
import com.braindelay.mp3scanner.dao.SongDAO;
import com.braindelay.mp3scanner.model.AlbumArtist;
import com.braindelay.mp3scanner.model.JobData;
import com.braindelay.mp3scanner.model.Song;
import com.braindelay.mp3scanner.services.Scanner;
import com.braindelay.mp3scanner.services.impl.jms.TaskQueueHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Daniel on 03/08/2015.
 */
@Service
public class ScannerImpl implements Scanner {

    protected final Log log = LogFactory.getLog(getClass());
    @Autowired
    private JobDAO jobDAO;

    @Autowired
    private SongDAO songDAO;
    @Autowired
    private TaskQueueHelper taskQueue;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<JobData> getCurrentJobs() {
        log.info("Getting current jobs");
        return jobDAO.getUIJobs(JobData.JobState.Created, JobData.JobState.Running);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<JobData> getAllJobs() {
        log.info("Getting all jobs");
        return jobDAO.getUIJobs(JobData.JobState.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startJob(String path) {
        log.debug(String.format("Creating new job for %s", path));
        JobData job = new JobData(JobData.JobType.UI);
        job.setPath(path);
        jobDAO.save(job);
        taskQueue.send(job);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelJob(JobData job) {
        job.setState(JobData.JobState.Cancelled);
        jobDAO.save(job);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void process(JobData jobData) {
        log.debug(String.format("Procesing job : %s", jobData));

        File check = new File(jobData.getPath());
        if (check.exists() && check.isDirectory()) {
            log.info(String.format("Scanning directory: %s", check.getPath()));
            File[] children = check.listFiles();
            if (null != children) {
                for (File child : children) {

                    try {
                        JobData task = jobData.createTask(child.getCanonicalPath());
                        taskQueue.send(task);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        } else {
            log.info(String.format("Storing file file: %s", check.getPath()));


            if (storeSong(jobData)) {
                jobDAO.incrementSongCount(jobData.getParentId(), check.getPath());
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobData getJob(ObjectId id) {
        return jobDAO.find(id);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean storeSong(JobData job) {
        log.debug(String.format("Checking job: %s", job.getPath()));
        if (job.getPath().toLowerCase().endsWith(".mp3")){
            try {

                MP3File mp3File = (MP3File)loadMP3(job);

                String artist = getField(mp3File, FieldKey.ARTIST);
                String album= getField(mp3File, FieldKey.ALBUM);
                String title= getField(mp3File, FieldKey.TITLE);
                String track= getField(mp3File,FieldKey.TRACK);

                Song song = new Song(artist,album,title, job.getPath(), track);
                log.debug(String.format("Saving : %s", job.getPath()));
                songDAO.save(song);
                return true;
            } catch (IOException e) {
                log.error(String.format("Problem loading file at %s", job.getPath()));
            } catch (TagException e) {
                log.error(String.format("Problem loading file at %s", job.getPath()));
            } catch (ReadOnlyFileException e) {
                log.error(String.format("Problem loading file at %s", job.getPath()));
            } catch (CannotReadException e) {
                log.error(String.format("Problem loading file at %s", job.getPath()));
            } catch (InvalidAudioFrameException e) {
                log.error(String.format("Problem loading file at %s", job.getPath()));
            }

        }
        return false;
    }

    /**
     * Check the underlying tags from the file and select the first one with a value
     * @param mp3File
     * @param key
     * @return
     */
    private String getField(MP3File mp3File, FieldKey key) {
        String value = "";
        if (mp3File.hasID3v2Tag()) {
            value = StringUtils.trimToEmpty(mp3File.getID3v2Tag().getFirst(key));
            if (!value.isEmpty()){
                return value;
            }
        }
        if (mp3File.hasID3v1Tag()){
            value = StringUtils.trimToEmpty(mp3File.getID3v1Tag().getFirst(key));
            if (!value.isEmpty()){
                return value;
            }
        }

        return StringUtils.trimToEmpty(mp3File.getTag().getFirst(key));
    }

    /**
     * Load the file as an MP3
     * @param job
     * @return
     * @throws CannotReadException
     * @throws IOException
     * @throws TagException
     * @throws ReadOnlyFileException
     * @throws InvalidAudioFrameException
     */
    protected AudioFile loadMP3(JobData job) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
        return AudioFileIO.read(new File(job.getPath()));
    }

}
