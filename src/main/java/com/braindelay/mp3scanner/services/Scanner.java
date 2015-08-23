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
    List<JobData> getCurrentJobs();
    List<JobData> getAllJobs();
    void startJob(String path);
    void cancelJob(JobData job);

    void process(JobData jobData);

    JobData getJob(ObjectId id);

    boolean storeSong(JobData job);


}
