package com.vance.diningphilosophers;

import java.util.concurrent.atomic.AtomicBoolean;

public class Fork {
    public static final int NOT_GRABBED = -1;

    private final int id;
    private final AtomicBoolean grabbed;
    private int grabbedBy = NOT_GRABBED;

    public Fork(int id) {
        this.id = id;
        this.grabbed = new AtomicBoolean();
    }

    public int getId() {
        return id;
    }

    /**
     * Grab the fork.
     *
     * @return true if the fork was successfully grabbed, and false otherwise.
     *
     * @param id the ID of the place for the philosopher claiming the fork
     */
    public synchronized boolean grabBy(int id) {
        if (grabbedBy == id) {
            return true;
        }
        if (grabbed.compareAndSet(false, true)) {
            grabbedBy = id;
            return true;
        }
        return false;
    }

    /**
     * Release the fork. Only tried if it is already grabbed.
     *
     * @param id the ID of the place for the philosopher claiming the fork
     */
    public synchronized boolean releaseBy(int id) {
        if (grabbedBy == id && grabbed.compareAndSet(true, false)) {
            grabbedBy = NOT_GRABBED;
            return true;
        }
        return false;
    }
}
