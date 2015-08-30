package com.braindelay.mp3scanner.dao.impl;

import com.braindelay.mp3scanner.model.AlbumArtist;
import com.braindelay.mp3scanner.model.Song;
import com.mongodb.BasicDBObject;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Daniel on 23/08/2015.
 */
public class SongDAOTest {
    private SongDAOImpl dao;

    @Before
    public void setup() {
        final MongoTemplate template= Mockito.mock(MongoTemplate.class);
        dao = new SongDAOImpl() {
            @Override
            protected MongoTemplate getModel() {
                return template;
            }
        };


        AggregationResults<Object> result = new AggregationResults<>(new ArrayList<>(), new BasicDBObject() );
        Mockito.when(template.aggregate(any(Aggregation.class),eq(dao.getBaseCollectionName()),any(Class.class))).thenReturn(result);

    }

    @Test
    public void testGetArtists() {
        dao.getArtists();

        ArgumentCaptor<Aggregation> queries = verifyAggregations(1, AlbumArtist.Artist.class);
        Aggregation aggregation = queries.getValue();

        Assert.assertTrue(StringUtils.contains(aggregation.toString(), "{ \"$group\" : { \"_id\" : \"$artist\"}} , { \"$sort\" : { \"_id\" : 1}}"));
    }



    @Test
    public void testGetAlbums() {

        dao.getAlbums(null);
        dao.getAlbums("");
        dao.getAlbums("Bowie,David");

        String emptyAggregation = "\"pipeline\" : [ { \"$group\" : { \"_id\" : { \"artist\" : \"$artist\" , \"album\" : \"$album\"}}} , { \"$sort\" : { \"_id.album\" : 1}}]";
        String bowieAggregation = "\"pipeline\" : [ { \"$match\" : { \"artist\" : \"Bowie,David\"}} , { \"$group\" : { \"_id\" : { \"artist\" : \"$artist\" , \"album\" : \"$album\"}}} , { \"$sort\" : { \"_id.album\" : 1}}]";

        ArgumentCaptor<Aggregation> queries = verifyAggregations(3, AlbumArtist.class);

        {
            Aggregation observedAggregation = queries.getAllValues().get(0);
            Assert.assertTrue(observedAggregation.toString(), StringUtils.contains(observedAggregation.toString(), emptyAggregation));
        }

        {
            Aggregation observedAggregation = queries.getAllValues().get(1);
            Assert.assertTrue(observedAggregation.toString(), StringUtils.contains(observedAggregation.toString(), emptyAggregation));
        }

        {
            Aggregation observedAggregation = queries.getAllValues().get(2);
            Assert.assertTrue(observedAggregation.toString(), StringUtils.contains(observedAggregation.toString(), bowieAggregation));
        }
    }

    @Test
    public void testSongSearch() {
        dao.getSongs(null,null);
        dao.getSongs("","");
        dao.getSongs("Bowie, David", "");
        dao.getSongs("", "Station to Station");
        dao.getSongs("Bowie, David", "Station to Station");

        String emptyQuery = "Query: { }";

        ArgumentCaptor<Query> queries= ArgumentCaptor.forClass(Query.class);
        verify(dao.getModel(), times(5)).find(queries.capture(), eq(Song.class), eq(dao.getBaseCollectionName()));

        {
            Query observedQuery = queries.getAllValues().get(0);
            Assert.assertTrue(observedQuery.toString(), StringUtils.contains(observedQuery.toString(), emptyQuery));
        }
        {
            Query observedQuery = queries.getAllValues().get(1);
            Assert.assertTrue(observedQuery.toString(), StringUtils.contains(observedQuery.toString(), emptyQuery));
        }
        {
            Query observedQuery = queries.getAllValues().get(2);
            Assert.assertTrue(observedQuery.toString(), StringUtils.contains(observedQuery.toString(), "Query: { \"artist\" : \"Bowie, David\"}"));
        }
        {
            Query observedQuery = queries.getAllValues().get(3);
            Assert.assertTrue(observedQuery.toString(), StringUtils.contains(observedQuery.toString(), "Query: { \"album\" : \"Station to Station\"}"));
        }
        {
            Query observedQuery = queries.getAllValues().get(4);
            Assert.assertTrue(observedQuery.toString(), StringUtils.contains(observedQuery.toString(), "Query: { \"artist\" : \"Bowie, David\" , \"album\" : \"Station to Station\"}"));
        }

    }
    private ArgumentCaptor<Aggregation> verifyAggregations(int expextedTimes,Class resultClass) {
        ArgumentCaptor<Aggregation> aggregations = ArgumentCaptor.forClass(Aggregation.class);

        verify(dao.getModel(), times(expextedTimes)).aggregate(aggregations.capture(),eq(dao.getBaseCollectionName()), eq(resultClass));
        return aggregations;
    }
}
