package com.braindelay.mp3scanner.dao.impl;

import com.braindelay.mp3scanner.dao.MongoDAO;
import com.mongodb.MongoClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by Daniel on 03/08/2015.
 */
abstract class AbstractMongoDAOImpl<T> implements MongoDAO<T> {
    private MongoTemplate mongoOps;
    private final Class<T> type;

    final Log log = LogFactory.getLog(getClass());

    public AbstractMongoDAOImpl(Class<T> type) {
        this.type=type;

        try {
            MongoClient mongoClient= new MongoClient();
            mongoOps = new MongoTemplate(mongoClient, "music");
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Can't get to mongo",e);
        }


    }

    MongoTemplate getModel() {
        return mongoOps;
    }

    abstract String getBaseCollectionName();


    public T find(ObjectId id)  {
        return getModel().findOne(Query.query(Criteria.where("_id").is(id)), type, getBaseCollectionName());
    }

    public void save(T pojo) {
        log.debug(String.format("Saving pojo %s <= %s", getBaseCollectionName(), pojo));
        getModel().save(pojo, getBaseCollectionName());

    }

    public List<T> find(Query query) {
        log.debug(String.format("Loading data from %s for query %s ", getBaseCollectionName(), query));
        List<T> results = getModel().find(query, type, getBaseCollectionName());
        log.debug(String.format("Loaded %s results from %s for query %s", results.size(), getBaseCollectionName(), query));
        return results;
    }

}
