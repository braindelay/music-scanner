package com.braindelay.mp3scanner.model;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * Created by Daniel on 03/08/2015.
 */
public class JobData implements Serializable {


    public interface Fields {
        static final String TYPE = "type";
        static final String STATE = "state";
        static final String SONG_FOUND = "songsFound";
        String LAST_SONG_FOUND = "lastSongFound";
    }

    public enum JobState {
        Created, Running, Completed, Errored, Cancelled;
    }

    public enum JobType {
        UI, Task
    }

    @Id
    private ObjectId id;

    private ObjectId parentId;
    private JobType type;
    private String path;
    private int songsFound;
    private int songsScanned;
    private String lastSongFound;
    private JobState state;


    private JobData() {
        // needed for spring data
    }

    /**
     * Build the job of a given type
     * @param type
     */
    public JobData(JobType type) {
        this();
        this.type = type;
        state = JobState.Created;
    }

    /**
     * Create a task from the given job
     * @param path
     * @return
     */
    public JobData createTask(String path) {
        JobData task = new JobData(JobType.Task);
        task.setPath(path);

        if (null == parentId) {
            task.parentId = id;
        } else {
            task.parentId = parentId;
        }

        return task;
    }

    /**
     * If this is a task, then the parent id is the base job id that this is a task for.
     * @return
     */
    public ObjectId getParentId() {
        return parentId;
    }

    /**
     * Get a count of how many songs were scanned
     * @return
     */
    public int getSongsScanned() {

        return songsScanned;
    }

    public void setSongsScanned(int songsScanned) {

        this.songsScanned = songsScanned;
    }

    public int getSongsFound() {
        return songsFound;
    }

    public void setSongsFound(int songsFound) {
        this.songsFound = songsFound;
    }


    public JobState getState() {
        return state;
    }

    public void setState(JobState state) {
        this.state = state;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getLastSongFound() {
        return lastSongFound;
    }

    public void setLastSongFound(String lastSongFound) {
        this.lastSongFound = lastSongFound;
    }

    @Override
    public String toString() {
        return String.format("%s; %s; %s; %s", type, state, id, path);
    }
}
