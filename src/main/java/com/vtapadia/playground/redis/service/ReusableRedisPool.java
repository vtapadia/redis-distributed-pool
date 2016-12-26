package com.vtapadia.playground.redis.service;

import com.vtapadia.playground.redis.data.Store;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Primary
public class ReusableRedisPool implements ReusablePool {

    private static final Logger logger = LoggerFactory.getLogger(ReusableRedisPool.class);
    private static final String KEY = "live.object.key";
    private static final String LOCK_KEY = KEY + ".lock";

    private RLock lock;
    private Store store;

    @Autowired
    public ReusableRedisPool(RedissonClient redissonClient) throws InterruptedException {
        RLiveObjectService service = redissonClient.getLiveObjectService();
        lock = redissonClient.getFairLock(LOCK_KEY);
        if (lock.tryLock(100, 10, TimeUnit.SECONDS)) {
            store = service.get(Store.class, KEY);
            if (store == null) {
                store = new Store(KEY);
                service.merge(store);
            }
        }
        lock.unlock();
    }

    public void addReusable(List<Reusable> reusables) {
        if (reusables == null) {
            return;
        }
        try {
            if (lock.tryLock(100, 10, TimeUnit.SECONDS)) {
                for (Reusable reusable : reusables) {
                    if (!store.getAvailable().contains(reusable) &&
                            !store.getReserved().contains(reusable)) {
                        store.getAvailable().offer(reusable);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Unable to acquire lock due to error ", e);
        }
        lock.unlock();
    }

    public Reusable acquire() {
        Reusable reusable = null;
        try {
            reusable = store.getAvailable().poll(10, TimeUnit.SECONDS);
            if (reusable != null) {
                store.getReserved().add(reusable);
            }
        } catch (Exception e) {
            logger.error("Unable to get any available object from the pool within the timeout settings", e);
        }
        logger.debug("Total Entities available " + store.getAvailable().size());
        logger.debug("Total Entities locked " + store.getReserved().size());
        return reusable;
    }

    public void release(Reusable reusable) {
        if (reusable != null) {
            store.getAvailable().offer(reusable);
            store.getReserved().remove(reusable);
        }
        logger.debug("Total Entities available " + store.getAvailable().size());
        logger.debug("Total Entities locked " + store.getReserved().size());
    }

}
