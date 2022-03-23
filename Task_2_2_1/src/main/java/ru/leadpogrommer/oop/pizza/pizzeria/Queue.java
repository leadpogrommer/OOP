package ru.leadpogrommer.oop.pizza.pizzeria;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Queue<T> {
    private final java.util.Queue<T> queue;
    int size;

    public Queue(int size) {
        queue = new ArrayDeque<>(size);
        this.size = size;
    }

    public Integer length() throws InterruptedException {
        synchronized (queue) {
            return queue.size();
        }
    }

    public T get() throws InterruptedException {
        synchronized (queue) {
            while (queue.isEmpty()) {
                queue.wait();
            }
            queue.notifyAll();
            return queue.remove();
        }
    }

    public List<T> getMany(int maxCount) throws InterruptedException {
        synchronized (queue) {
            while (queue.isEmpty()) {
                queue.wait();
            }
            var res = new ArrayList<T>(maxCount);
            while (res.size() < maxCount && !queue.isEmpty()) {
                res.add(queue.remove());
            }
            queue.notifyAll();
            return res;
        }
    }

    public void put(T order) throws InterruptedException {
        synchronized (queue) {
            while (queue.size() >= size) {
                queue.wait();
            }
            if (!queue.add(order)) {
                throw new IllegalStateException("Element was not added");
            }
            queue.notifyAll();
        }
    }
}
