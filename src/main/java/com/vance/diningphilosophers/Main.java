package com.vance.diningphilosophers;

public class Main {

    public static void main(String[] args) {
        Table table = new Table(Tuneables.PHILOSOPHER_COUNT);

        try {
            table.startDinner();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        System.out.println("Finished");
    }

}
