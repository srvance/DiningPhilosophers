package com.vance.diningphilosophers;

public class DeadlockDetectorRunnable implements Runnable {
    private int lastConsumedTotal = 0;
    private Table table;

    public DeadlockDetectorRunnable(Table table) {
        this.table = table;
    }

    @Override
    public void run() {
        System.err.println("*** Checking for consumption");
        int currentTotal = Place.totalFood.get();
        if (currentTotal == lastConsumedTotal) {
            System.err.println("*** DEADLOCK DETECTED!!!");
            table.endDinner();
        } else {
            lastConsumedTotal = currentTotal;
        }
    }
}
