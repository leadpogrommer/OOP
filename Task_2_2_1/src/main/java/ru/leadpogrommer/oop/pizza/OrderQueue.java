package ru.leadpogrommer.oop.pizza;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class OrderQueue<T> {
    private final Queue<T> queue;
    int size;

    OrderQueue(int size) {
        queue = new ArrayDeque<>(size);
        this.size = size;
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
