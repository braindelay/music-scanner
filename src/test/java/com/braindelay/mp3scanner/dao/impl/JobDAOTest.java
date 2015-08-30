package com.braindelay.mp3scanner.dao.impl;

import com.braindelay.mp3scanner.model.JobData;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;

/**
 * Created by Daniel on 22/08/2015.
 */


public class JobDAOTest {

    private JobDAOImpl dao;

    @Before
    public void setup() {
        MongoTemplate template = Mockito.mock(MongoTemplate.class);
        dao = new JobDAOImpl() {
            // mockito refused to spy this
            @Override
            protected MongoTemplate getModel() {
                return template;
            }
        };


    }

    @Test
    public void testGetUIJobs() {

        dao.getUIJobs(JobData.JobState.Created, JobData.JobState.Running);

        ArgumentCaptor<Query> queries = ArgumentCaptor.forClass(Query.class);
        Mockito.verify(dao.getModel(), times(1)).find(queries.capture(), eq(JobData.class), eq(dao.getBaseCollectionName()));

        Query query = queries.getValue();

        Assert.assertEquals(JobData.JobType.UI, query.getQueryObject().get(JobData.Fields.TYPE));
        List<JobData.JobState> requestedStates = (List<JobData.JobState>) ((BasicDBObject) query.getQueryObject().get(JobData.Fields.STATE)).get("$in");
        Assert.assertEquals(Arrays.asList(JobData.JobState.Created, JobData.JobState.Running), requestedStates);

    }

    @Test
    public void testIncrementSongCount() {
        ObjectId id = new ObjectId();
        dao.incrementSongCount(id, "a song");

        ArgumentCaptor<Query> query = ArgumentCaptor.forClass(Query.class);
        ArgumentCaptor<Update> update = ArgumentCaptor.forClass(Update.class);

        Mockito.verify(dao.getModel(), times(1)).updateFirst(query.capture(), update.capture(), eq(dao.getBaseCollectionName()));
    }



}