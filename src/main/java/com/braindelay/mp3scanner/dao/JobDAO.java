package com.braindelay.mp3scanner.dao;

import com.braindelay.mp3scanner.model.JobData;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by Daniel on 03/08/2015.
 */
public interface JobDAO extends MongoDAO<JobData>{

    List<JobData> getUIJobs(JobData.JobState... values);


    void incrementSongCount(ObjectId parentId, String path);
}
