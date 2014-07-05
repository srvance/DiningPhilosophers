package com.vance.diningphilosophers;

import java.util.concurrent.atomic.AtomicInteger;

public class Place {
    public static final AtomicInteger totalFood = new AtomicInteger();

    private final int id;
    private final Fork leftFork;
    private final Fork rightFork;
    private final AtomicInteger food;

    public Place(int id, Fork leftFork, Fork rightFork) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.food = new AtomicInteger();
    }

    public int getId() {
        return id;
    }

    public Fork getLeftFork() {
        return leftFork;
    }

    public Fork getRightFork() {
        return rightFork;
    }

    public int getFoodEaten() {
        return food.get();
    }

    public int consumeFood() {
        totalFood.incrementAndGet();
        return food.incrementAndGet();
    }
}
