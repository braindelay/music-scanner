package com.braindelay.mp3scanner.services.impl.jms;

import com.braindelay.mp3scanner.dao.JobDAO;
import com.braindelay.mp3scanner.model.JobData;
import com.braindelay.mp3scanner.services.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by Daniel on 09/08/2015.
 */
@Component
public class TaskQueueHelper extends JmsHelper<JobData> {
    private final static String TASK_QUEUE = "tasks";

    protected TaskQueueHelper() {
        super(TASK_QUEUE);
    }

    @Autowired
    private Scanner scanner;


    /**
     * Process the task job - that is, scan the path for more tasks to process
     * @param job
     */
    @Override
    @JmsListener(destination = TASK_QUEUE)
    public void receiveMessage(JobData job) {
        log.debug(String.format("Received message on queue [%s]: [%s]", getQueueName(), job));
        scanner.process(job);
    }
}
