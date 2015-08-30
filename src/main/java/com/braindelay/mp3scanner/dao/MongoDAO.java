package com.braindelay.mp3scanner.dao;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by Daniel on 09/08/2015.
 */
public interface MongoDAO<T> {
    /**
     * Find the field with the given id.
     * @param id
     * @return
     */
    T find(ObjectId id) ;

    /**
     * Save the pojo
     * @param pojo
     */
    void save(T pojo) ;


    /**
     * Find all the events for the given query
     * @param query
     * @return
     */
    List<T> find(Query query) ;

}
