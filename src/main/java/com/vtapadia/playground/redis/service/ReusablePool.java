package com.vtapadia.playground.redis.service;

import java.util.List;

public interface ReusablePool {
    /**
     * Add elements to the distributed pool.
     * @param reusables - list of items that needs to be added to the pool
     */
    void addReusable(List<Reusable> reusables);

    /**
     * Acquire a pooled object.
     * @return Reusable object if able to retrieve within the time defined.
     */
    Reusable acquire();

    /**
     * Returns a object back to the pool
     * @param reusable - object that needs to be returned
     */
    void release(Reusable reusable);
}
