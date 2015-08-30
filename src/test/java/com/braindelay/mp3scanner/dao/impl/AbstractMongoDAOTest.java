package com.braindelay.mp3scanner.dao.impl;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;

/**
 * Created by Daniel on 29/08/2015.
 */
public class AbstractMongoDAOTest {

    private AbstractMongoDAOImpl<String> test;

    @Before
    public void setup() {
        MongoTemplate template = Mockito.mock(MongoTemplate.class);
        test = new AbstractMongoDAOImpl<String>(String.class){
            @Override
            String getBaseCollectionName() {
                return "testCollection";
            }

            @Override
            protected MongoTemplate getModel() {
                return template;
            }
        };
    }

    @Test
    public void testSave() {
        test.save("hello");
        Mockito.verify(test.getModel(), times(1)).save("hello","testCollection");
    }
    @Test
    public void testFindQuery() {

        Query query = Query.query(Criteria.where("id").is("a value"));
        test.find(query);
        Mockito.verify(test.getModel(), times(1)).find(query, String.class, "testCollection");
    }

    @Test
    public void testFindId() {

        ObjectId id = new ObjectId();


        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
        test.find(id);
        Mockito.verify(test.getModel(),times(1)).findOne(queryCaptor.capture(), eq(String.class), eq("testCollection"));

        Query expectedQuery = Query.query(Criteria.where("_id").is(id));
        Assert.assertEquals(expectedQuery.toString(), queryCaptor.getValue().toString());
    }


}
