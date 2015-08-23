package com.braindelay.mp3scanner.services.impl.jms;

import com.braindelay.mp3scanner.model.JobData;
import com.braindelay.mp3scanner.services.Scanner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;

/**
 * Created by Daniel on 23/08/2015.
 */
@RunWith(value = MockitoJUnitRunner.class)
public class TaskQueueTest {

    @InjectMocks
    TaskQueueHelper mock = new TaskQueueHelper();

    @Mock
    Scanner scanner;


    @Test
    public void test() {
        JobData job = new JobData(JobData.JobType.Task);
        mock.receiveMessage(job);

        Mockito.verify(scanner,times(1)).process(job);
    }
}
