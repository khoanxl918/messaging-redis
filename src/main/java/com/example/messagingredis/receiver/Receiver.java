package com.example.messagingredis.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class Receiver {
    public static final Logger logger = LoggerFactory.getLogger(Receiver.class);

    private AtomicInteger counter = new AtomicInteger();

    public void receiveMessage(String message) {
        logger.info("Received <" + message + ">");
        counter.incrementAndGet();
    }

    public int getCounter() {
        return counter.get();
    }
}
