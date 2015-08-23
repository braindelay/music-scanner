package com.braindelay.mp3scanner.services.impl.jms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Session;
import java.io.Serializable;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by Daniel on 23/08/2015.
 */
@RunWith(value= MockitoJUnitRunner.class)
public class JmsHelperTest {


    @Mock
    private JmsTemplate jmsTemplate;

    @InjectMocks
    private JmsHelper mock = new JmsHelper("a queue") {
        @Override
        public void receiveMessage(Serializable pojo) {

        }
    };

    @Test
    public void test() throws JMSException {
        mock.send("hello");

        // this is checking we created the correcy message creator
        ArgumentCaptor<MessageCreator> creator = ArgumentCaptor.forClass(MessageCreator.class);
        verify(jmsTemplate, times(1)).send(eq("a queue"), creator.capture());

        // this is checking we actually sent the correct message
        Session session = mock(Session.class);
        creator.getValue().createMessage(session);
        verify(session, times(1)).createObjectMessage("hello");

    }
}
