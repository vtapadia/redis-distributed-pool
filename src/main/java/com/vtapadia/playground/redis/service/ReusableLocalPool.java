package com.vtapadia.playground.redis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
@Profile("local")
public class ReusableLocalPool implements ReusablePool {
    private static Logger logger = LoggerFactory.getLogger(ReusableLocalPool.class);

    BlockingQueue<Reusable> queue = new LinkedBlockingQueue<>();

    @Override
    public void addReusable(List<Reusable> reusables) {
        reusables.stream()
                .filter(r -> !queue.contains(r))
                .forEach(r -> queue.offer(r));
        ;

        queue.addAll(reusables);
    }

    @Override
    public Reusable acquire() {
        try {
            return queue.poll(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("Unable to acquire an object in specified time", e);
        }
        return null;
    }

    @Override
    public void release(Reusable reusable) {
        if (reusable != null) {
            queue.offer(reusable);
        }
    }
}
