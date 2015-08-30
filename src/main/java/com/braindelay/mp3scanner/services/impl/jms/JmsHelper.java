package com.braindelay.mp3scanner.services.impl.jms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.Serializable;

/**
 * Created by Daniel on 08/08/2015.
 */

abstract class JmsHelper<T extends Serializable> {
    final Log log = LogFactory.getLog(getClass());

    @Autowired
    private JmsTemplate jmsTemplate;

    private final String queueName;

    /**
     * Build a JmsHelper for the given queue.
     * @param queueName
     */
    JmsHelper(String queueName) {
        this.queueName = queueName;
    }

    /**
     * Get the queue name for the helper
     * @return
     */
    String getQueueName() {
        return queueName;
    }

    /**
     * Receive an object message from the named queue
     * @param pojo
     */
    abstract public void receiveMessage(T pojo);


    /**
     * Send an object message over the named queue
     * @param message
     */
    public void send(final T message){
        MessageCreator messageCreator = session -> {
            log.debug(String.format("Sending message to queue [%s]: [%s]", queueName, message));
            return session.createObjectMessage(message);
        };
        jmsTemplate.send(queueName, messageCreator);
    }


}
