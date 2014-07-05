package com.vance.diningphilosophers;

public class StarvationDetectorRunnable implements Runnable {
    private final Table table;
    private final int[] amountLastEaten;

    public StarvationDetectorRunnable(Table table) {
        this.table = table;
        Place[] places = table.getPlaces();
        amountLastEaten = new int[places.length];
    }

    @Override
    public void run() {
        Place[] places = table.getPlaces();
        for (int placeId = 0; placeId < places.length; placeId++) {
            System.err.println("*** Checking for starvation at place " + placeId);
            int currentTotal = places[placeId].getFoodEaten();
            if (currentTotal == amountLastEaten[placeId]) {
                System.err.println("*** STARVATION DETECTED at " + placeId);
                table.endDinner();
            } else {
                amountLastEaten[placeId] = currentTotal;
            }

        }
    }
}
