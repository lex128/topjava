package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TimeUtil {
    private Integer count;
    private final AtomicBoolean excess;
    private List<UserMealWithExcess> meals;

    public TimeUtil(Integer count, AtomicBoolean excess, List<UserMealWithExcess> meals) {
        this.count = count;
        this.excess = excess;
        this.meals = meals;
    }

    public Integer getCount() {
        return this.count;
    }

    public AtomicBoolean getExcess() {
        return this.excess;
    }

    public List<UserMealWithExcess> getMeals() {
        return this.meals;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setExcess(boolean excess) {
        this.excess.set(excess);
    }

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }
}
