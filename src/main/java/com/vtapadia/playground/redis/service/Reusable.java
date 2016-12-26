package com.vtapadia.playground.redis.service;

public class Reusable {
    private String value;

    public Reusable() {
    }

    public Reusable(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reusable reusable = (Reusable) o;

        return value != null ? value.equals(reusable.value) : reusable.value == null;

    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
