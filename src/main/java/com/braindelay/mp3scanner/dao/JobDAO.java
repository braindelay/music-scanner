package com.braindelay.mp3scanner.dao;

import com.braindelay.mp3scanner.model.JobData;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by Daniel on 03/08/2015.
 */
public interface JobDAO extends MongoDAO<JobData>{

    /**
     * Get all the jobs (with thge UI type) that are in any of the given states
     * @param values
     * @return
     */
    List<JobData> getUIJobs(JobData.JobState... values);


    /**
     * Increment the song count for the identified job, and also mark what the last job was.
     * @param parentId
     * @param path
     */
    void incrementSongCount(ObjectId parentId, String path);
}
