package ru.leadpogrommer.oop.pizza.pizzeria.worker;

public abstract class Worker implements Runnable {
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                loop();
            }
        } catch (InterruptedException ignored) {
        }
    }

    abstract void loop() throws InterruptedException;
}