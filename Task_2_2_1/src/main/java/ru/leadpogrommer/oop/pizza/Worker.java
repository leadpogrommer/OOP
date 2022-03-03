package ru.leadpogrommer.oop.pizza;

abstract class Worker implements Runnable {
    @Override
    public void run() {
        try {
            while (true) {
                loop();
            }
        } catch (InterruptedException ignored) {
        }
    }

    abstract void loop() throws InterruptedException;
}