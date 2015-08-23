package com.braindelay.mp3scanner.dao;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by Daniel on 09/08/2015.
 */
public interface MongoDAO<T> {
    T find(ObjectId id) ;

    void save(T pojo) ;

    List<T> findAll();

    List<T> find(Query query) ;

}
