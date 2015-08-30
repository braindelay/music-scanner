package com.braindelay.mp3scanner.dao.impl;

import com.braindelay.mp3scanner.dao.SongDAO;
import com.braindelay.mp3scanner.model.AlbumArtist;
import com.braindelay.mp3scanner.model.Song;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 03/08/2015.
 */
@Component
public class SongDAOImpl extends AbstractMongoDAOImpl<Song> implements SongDAO {
    public SongDAOImpl() {
        super(Song.class);
    }

    @Override
    String getBaseCollectionName() {
        return "songs";
    }


    @Override
    public List<AlbumArtist.Artist> getArtists() {
        // db.songs.aggregate([ {$group :{_id : { artist : "$artist"}}}, {$sort: {_id: 1}} ])
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.group(Song.Fields.ARTIST),
                Aggregation.sort(Sort.Direction.ASC, "_id")
        );
        AggregationResults<AlbumArtist.Artist> result = getModel().aggregate(agg, getBaseCollectionName(), AlbumArtist.Artist.class);
        return result.getMappedResults();
    }

    @Override
    public List<AlbumArtist> getAlbums(String artist) {

        // db.songs.aggregate([ {$group :{_id : { artist : "$artist", album: "$album"}}}, {$sort: {_id: 1}} ])
        List<AggregationOperation> operations = new ArrayList<>();

        if (!StringUtils.isBlank(artist)) {
            operations.add(Aggregation.match(Criteria.where(Song.Fields.ARTIST).is(artist)));
        }
        operations.add(Aggregation.group(Song.Fields.ARTIST, Song.Fields.ALBUM));
        operations.add(Aggregation.sort(Sort.Direction.ASC, Song.Fields.ALBUM));
        Aggregation agg = Aggregation.newAggregation(operations.toArray(new AggregationOperation[operations.size()]));


        AggregationResults<AlbumArtist> result = getModel().aggregate(agg, getBaseCollectionName(), AlbumArtist.class);

        return result.getMappedResults();
    }


    @Override
    public List<Song> getSongs(String artist, String album) {
        Query query = new Query();
        if (!StringUtils.isBlank(artist)) {
            query.addCriteria(Criteria.where(Song.Fields.ARTIST).is(artist));
        }
        if (!StringUtils.isBlank(album)) {
            query.addCriteria(Criteria.where(Song.Fields.ALBUM).is(album));
        }
        query.with(new Sort(Sort.Direction.ASC,Song.Fields.TITLE));
        return find(query);
    }
}
