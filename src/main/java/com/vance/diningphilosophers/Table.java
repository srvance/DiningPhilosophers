package com.vance.diningphilosophers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Table {
    private final int placesCount;
    private final Place[] places;
    private final Philosopher[] philosophers;
    private ScheduledFuture<?> deadlockFuture;
    private ScheduledFuture<?> starvationFuture;
    private List<Thread> threads;
    private ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1);

    public Table(int placesCount) {
        this.placesCount = placesCount;

        places = setPlaces();

        philosophers = seatDiners(places);
    }

    public Place[] getPlaces() {
        return places;
    }

    private Place[] setPlaces() {
        Fork[] forks = new Fork[placesCount];
        for (int place = 0; place < forks.length; place++) {
            forks[place] = new Fork(place);
        }

        Place[] places = new Place[placesCount];
        for (int place = 0; place < forks.length; place++) {
            Fork leftFork = forks[place];
            Fork rightFork = forks[(place + 1) % forks.length];
            places[place] = new Place(place, leftFork, rightFork);
        }
        return places;
    }

    private Philosopher[] seatDiners(Place[] places) {
        Philosopher[] philosophers = new Philosopher[placesCount];
        for (int place = 0; place < places.length; place++) {
            philosophers[place] = new Philosopher(places[place]);
        }
        return philosophers;
    }

    public void startDinner() throws InterruptedException {
        threads = new ArrayList<Thread>(placesCount);
        for (Philosopher philosopher : philosophers) {
            threads.add(new Thread(new PhilosopherRunner(philosopher)));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        configureDeadlockDetection();
        configureStarvationDetection();

        for (Thread thread : threads) {
            thread.join();
        }
    }

    public void endDinner() {
        deadlockFuture.cancel(false);
        starvationFuture.cancel(false);
        for (Thread thread : threads) {
            thread.interrupt();
        }
        // TODO: I shouldn't have to exit here, but it hangs if I don't. Missing something.
        System.exit(0);
    }

    private void configureDeadlockDetection() {
        DeadlockDetectorRunnable deadlockDetector = new DeadlockDetectorRunnable(this);
        deadlockFuture = scheduledExecutor.scheduleAtFixedRate(deadlockDetector, Tuneables.DEADLOCK_DETECTION_INTERVAL_SECONDS, Tuneables.DEADLOCK_DETECTION_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    private void configureStarvationDetection() {
        StarvationDetectorRunnable starvationDetector = new StarvationDetectorRunnable(this);
        starvationFuture = scheduledExecutor.scheduleAtFixedRate(starvationDetector, Tuneables.STARVATION_DETECTION_INTERVAL_SECONDS, Tuneables.STARVATION_DETECTION_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }
}