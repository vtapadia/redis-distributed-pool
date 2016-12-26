package com.vtapadia.playground.redis.data;

import com.vtapadia.playground.redis.service.Reusable;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@REntity
public class Store {
    @RId
    private String id;

    private BlockingQueue<Reusable> available = new LinkedBlockingQueue<>();
    private List<Reusable> reserved = new ArrayList<>();

    public Store() {
    }

    public Store(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public BlockingQueue<Reusable> getAvailable() {
        return available;
    }

    public List<Reusable> getReserved() {
        return reserved;
    }

}
