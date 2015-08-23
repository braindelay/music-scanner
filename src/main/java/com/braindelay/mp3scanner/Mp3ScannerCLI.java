package com.braindelay.mp3scanner;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;

import javax.jms.ConnectionFactory;
import java.util.logging.Logger;


/**
 * Simple spring boot to start the application
 */
@SpringBootApplication
@EnableJms
public class Mp3ScannerCLI {

    private static Logger log = Logger.getLogger(Mp3ScannerCLI.class.getName());


    public static void main(String[] args) {
        log.info("Starting now");
        SpringApplication.run(Mp3ScannerCLI.class, args);
    }
}
