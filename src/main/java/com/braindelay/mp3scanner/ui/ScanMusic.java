package com.braindelay.mp3scanner.ui;

import com.braindelay.mp3scanner.model.AlbumArtist;
import com.braindelay.mp3scanner.model.JobData;
import com.braindelay.mp3scanner.model.Song;
import com.braindelay.mp3scanner.services.MusicDatabase;
import com.braindelay.mp3scanner.services.Scanner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * Rest API for the app
 */
@RestController()
public class ScanMusic {

    protected final Log log = LogFactory.getLog(getClass());
    @Autowired
    private Scanner scanner;

    @Autowired
    private MusicDatabase musicDatabase;


    /**
     * Get a list of all the current jobs
     *
     * @return
     */
    @RequestMapping("/scan")
    public List<JobData> getCurrentJobs() {
        log.debug("Getting current jobs");
        return scanner.getCurrentJobs();
    }

    /**
     * Start a scan for a given directory - this will scan the requested
     * directory, and all the subdirectories (and then repeats for all of
     * these)
     *
     * @param job
     */
    @RequestMapping(value = "/scan/directory", method = RequestMethod.POST)
    public void scanDirectory(@RequestBody JobData job) {
        log.debug(String.format("Scanning directory %s", job.getPath()));
        scanner.startJob(job.getPath());
    }

    /**
     * Get the data for yhe given job
     *
     * @param id
     * @return
     */
    @RequestMapping("/scan/job")
    public JobData getJobData(@RequestParam(value = "id") ObjectId id) {
        return scanner.getJob(id);
    }


    /**
     * A list of all the artists in the database
     *
     * @return
     */
    @RequestMapping(value = "/music/library/artists", method = RequestMethod.GET)
    public List<AlbumArtist.Artist> getArtists() {
        return musicDatabase.getArtists();
    }

    /**
     * A list of all the albums in the database
     *
     * @return
     */
    @RequestMapping(value = "/music/library/albums", method = RequestMethod.GET)
    public List<AlbumArtist> getAlbums() {
        return musicDatabase.getAlbums(null);
    }


    /**
     * A list of all the songs in the database
     *
     * @return
     */
    @RequestMapping(value = "/music/library/songs", method = RequestMethod.GET)
    public List<Song> getSongs() {
        return musicDatabase.getSongs(null, null);
    }



    @RequestMapping(value = "/music/download/{songId}", method = RequestMethod.GET)
    public void getFile(
            @PathVariable("songId") String rawSongId,
            HttpServletResponse response) throws IOException {


        ObjectId songId = new ObjectId(rawSongId);
        Song song = musicDatabase.get(songId);
        if (null == song) {
            log.warn(String.format("No song found for id %s", rawSongId));
        }else {
            log.debug(String.format("Streaming song id %s :%s", rawSongId,song));
            try (InputStream in = new BufferedInputStream(new FileInputStream(song.getPath()));) {
                IOUtils.copyLarge(in, response.getOutputStream());
                response.flushBuffer();
            }
        }
    }
}
