package com.braindelay.mp3scanner.services;

import com.braindelay.mp3scanner.model.AlbumArtist;
import com.braindelay.mp3scanner.model.JobData;
import com.braindelay.mp3scanner.model.Song;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by Daniel on 03/08/2015.
 */
public interface Scanner {
    /**
     * Get all the currently active jobs in the model
     * @return
     */
    List<JobData> getCurrentJobs();

    /**
     * Get all the jobs in the model
     * @return
     */
    List<JobData> getAllJobs();

    /**
     * Start a job for the identified path
     * @param path
     */
    void startJob(String path);

    /**
     * Cancel the job
     * @param job
     */
    void cancelJob(JobData job);

    /**
     * Process the specific job
     * @param jobData
     */
    void process(JobData jobData);

    /**
     * Get the identified job
     * @param id
     * @return
     */
    JobData getJob(ObjectId id);

    /**
     * Store the song mentioned in the identified job
     * @param job
     * @return
     */
    boolean storeSong(JobData job);

}
