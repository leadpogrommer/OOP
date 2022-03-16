package ru.leadpogrommer.oop.pizza.pizzeria;

import ru.leadpogrommer.oop.pizza.pizzeria.worker.DeliveryWorker;
import ru.leadpogrommer.oop.pizza.pizzeria.worker.DispatcherWorker;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Pizzeria {
    private final Queue<Order> orderQueue;
    private final Queue<Order> pizzaQueue;

    private final ThreadPoolExecutor cookPool;
    private final ThreadPoolExecutor deliveryPool;
    int nextId = 0;

    public Pizzeria(Config config) {
        cookPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        deliveryPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(config.deliveryMen.length);

        Arrays.sort(config.cooks, Comparator.comparingInt(a -> a.cookTime));

        orderQueue = new Queue<>(100);
        pizzaQueue = new Queue<>(config.storageSize);

        for (var dc : config.deliveryMen) {
            deliveryPool.submit(new DeliveryWorker(dc, pizzaQueue));
        }

        cookPool.submit(new DispatcherWorker(config, orderQueue, pizzaQueue, cookPool));

    }

    public static void log(int orderId, String message) {
        System.out.printf("[%d] %s%n", orderId, message);
    }

    public void placeOrder(String address) throws InterruptedException {
        log(nextId, "Accepted");
        orderQueue.put(new Order(nextId++, address));
    }

    public void stop() {
        cookPool.shutdownNow();
        deliveryPool.shutdownNow();
    }

}
