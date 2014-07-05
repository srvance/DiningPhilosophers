package com.vance.diningphilosophers;

public class PhilosopherRunner implements Runnable {
    private final Philosopher philosopher;

    public PhilosopherRunner(Philosopher philosopher) {
        this.philosopher = philosopher;
    }

    @Override
    public void run() {
        philosopher.dine();
    }
}
