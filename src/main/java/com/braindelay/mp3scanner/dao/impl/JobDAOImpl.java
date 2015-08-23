package com.braindelay.mp3scanner.dao.impl;

import com.braindelay.mp3scanner.dao.JobDAO;
import com.braindelay.mp3scanner.model.JobData;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Daniel on 03/08/2015.
 */
@Component
public class JobDAOImpl extends AbstractMongoDAOImpl<JobData> implements JobDAO {


    public JobDAOImpl() {
        super(JobData.class);
    }

    @Override
    String getBaseCollectionName() {
        return "jobs";
    }

    @Override
    public List<JobData> getUIJobs(JobData.JobState... values) {
        log.debug(String.format("Getting UI jobs in state: [%s]", Arrays.asList(values)));

        Query query = Query.query(
                // limit to UI jobs
                Criteria.where(JobData.Fields.TYPE).is(JobData.JobType.UI)
                        // limit to states of interest
                        .and(JobData.Fields.STATE).in(values)
        );
        List<JobData> jobs =  find(query);
        log.debug(String.format("Found %s results", jobs.size()));
        return jobs;
    }

    @Override
    public void incrementSongCount(ObjectId parentId, String path) {
        log.debug(String.format("Incrementing song count for job: %s", parentId));
        getModel().updateFirst(
                // find the instance
                Query.query(Criteria.where("_id").is(parentId)),
                // update last song count
                new Update().inc(JobData.Fields.SONG_FOUND, 1)
                        // and the last updated file
                        .set(JobData.Fields.LAST_SONG_FOUND, path)
                ,
                // instance.
                getBaseCollectionName());
    }


}
