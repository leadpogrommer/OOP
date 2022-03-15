package ru.leadpogrommer.oop.pizza;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Queue<T> {
    private final java.util.Queue<T> queue;
    int size;

    Queue(int size) {
        queue = new ArrayDeque<>(size);
        this.size = size;
    }

    Integer length() throws InterruptedException {
        synchronized (queue) {
            return queue.size();
        }
    }

    T get() throws InterruptedException {
        synchronized (queue) {
            while (queue.isEmpty()) {
                queue.wait();
            }
            queue.notify();
            return queue.remove();
        }
    }

    List<T> getMany(int maxCount) throws InterruptedException {
        synchronized (queue) {
            while (queue.isEmpty()) {
                queue.wait();
            }
            var res = new ArrayList<T>(maxCount);
            while (res.size() < maxCount && !queue.isEmpty()) {
                res.add(queue.remove());
            }
            return res;
        }
    }

    void put(T order) throws InterruptedException {
        synchronized (queue) {
            while (queue.size() >= size) {
                queue.wait();
            }
            if (!queue.add(order)) {
                throw new IllegalStateException("Element was not added");
            }
            queue.notify();
        }
    }
}
