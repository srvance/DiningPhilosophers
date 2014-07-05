package com.vance.diningphilosophers;

import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

public class Philosopher {
    public static final PrintStream OUTPUT = System.out;
    private final Place place;
    private final int id;

    public Philosopher(Place place) {
        this.place = place;
        this.id = place.getId();
    }

    public void dine() {
        boolean keepRunning = true;
        while (keepRunning) {
            if (acquireForks()) {
                keepRunning = eat();
                releaseForks();
            }
            keepRunning &= think();
        }
    }

    private boolean acquireForks() {
        // This naive algorithm guarantees deadlock pretty quickly
        return grab(getLeftFork()) && grab(getRightFork());
    }

    private void releaseForks() {
        release(getRightFork());
        release(getLeftFork());
    }

    /**
     * Gets the fork to the left.
     *
     * @return the fork to the left
     */
    public Fork getLeftFork() {
        return place.getLeftFork();
    }

    /**
     * Gets the fork to the right.
     *
     * @return the fork to the right
     */
    public Fork getRightFork() {
        return place.getRightFork();
    }

    /**
     * Thinks (sleeps) for a little (.5 s).
     *
     * @return true to indicate execution should continue and false to indicate it should stop
     */
    public boolean think() {
        try {
            OUTPUT.println(id + ": Thinking");
            TimeUnit.MILLISECONDS.sleep(Tuneables.THINK_DURATION_MILLIS);
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    /**
     * Consumes the food at the place as a time-taking (.1 s) activity.
     * Note that food is "consumed" before the wait.
     *
     * @return true to indicate execution should continue and false to indicate it should stop
     */
    public boolean eat() {
        int consumed = place.consumeFood();
        try {
            OUTPUT.println(id + ": Consumed " + consumed);
            TimeUnit.MILLISECONDS.sleep(Tuneables.EAT_DURATION_MILLIS);
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    /**
     * Grabs the specified fork if available.
     *
     * @param forkToGrab the fork to grab
     *
     * @return true if grabbed and false otherwise
     */
    private boolean grab(Fork forkToGrab) {
        OUTPUT.format("%d: Grabbing fork %d\n", id, forkToGrab.getId());

        boolean grabbed = forkToGrab.grabBy(id);

        OUTPUT.format("%d: %s fork %d\n", id, grabbed ? "Grabbed" : "Missed", forkToGrab.getId());
        return grabbed;
    }

    /**
     * Releases the specified fork. Does nothing if the fork is not grabbed by this Philosopher.
     *
     * @param forkToRelease the fork to release
     */
    public void release(Fork forkToRelease) {
        if (forkToRelease.releaseBy(id)) {
            OUTPUT.format("%d: Released fork %d\n", id, forkToRelease.getId());
        }
    }
}
