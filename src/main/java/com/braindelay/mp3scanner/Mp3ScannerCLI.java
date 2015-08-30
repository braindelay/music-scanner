package com.braindelay.mp3scanner;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

import java.util.logging.Logger;


/**
 * Simple spring boot to start the application
 */
@SpringBootApplication
@EnableJms
class Mp3ScannerCLI {

    private static Logger log = Logger.getLogger(Mp3ScannerCLI.class.getName());


    public static void main(String[] args) {
        log.info("Starting now");
        SpringApplication.run(Mp3ScannerCLI.class, args);
    }
}
