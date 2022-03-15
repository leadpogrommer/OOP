package ru.leadpogrommer.oop.pizza;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Pizzeria {
    private final Queue<Order> orderQueue;
    private final Queue<Order> pizzaQueue;
    private final Queue<Integer> freeCookQueue;
    private final ThreadPoolExecutor cookPool;
    private final ThreadPoolExecutor deliveryPool;
    private final Config config;
    int nextId = 0;

    Pizzeria(Config config) {
        cookPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        deliveryPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(config.deliveryMen.length);

        orderQueue = new Queue<>(100);
        pizzaQueue = new Queue<>(config.storageSize);
        freeCookQueue = new Queue<>(100);

        for (var dc : config.deliveryMen) {
            deliveryPool.submit(new DeliveryWorker(dc));
        }

        this.config = config;
        cookPool.submit(new Dispatcher());

    }

    static void log(int orderId, String message) {
        System.out.printf("[%d] %s%n", orderId, message);
    }

    void placeOrder(String address) throws InterruptedException {
        log(nextId, "Accepted");
        orderQueue.put(new Order(nextId++, address));
    }

    void stop() {
        cookPool.shutdownNow();
        deliveryPool.shutdownNow();
    }

    class CookWorker implements Runnable {
        final Config.Cook config;
        final Order order;

        CookWorker(Config.Cook c, Order order, Queue<Order> storage) {
            this.config = c;
            this.order = order;
        }

        @Override
        public void run() {
            try {
                log(order.id(), config.name + " started cooking");
                Thread.sleep(config.cookTime * 1000L);
                log(order.id(), config.name + " finished cooking");
                pizzaQueue.put(order);
                log(order.id(), config.name + " put pizza to storage");
            } catch (InterruptedException ignored) {
            }

        }
    }

    class DeliveryWorker extends Worker {
        final Config.DeliveryMan config;

        DeliveryWorker(Config.DeliveryMan c) {
            this.config = c;
        }

        @Override
        void loop() throws InterruptedException {
            var orders = pizzaQueue.getMany(config.trunkSize);
            System.out.println(config.name + " got pizzas: " + orders.size());
            for (var order : orders) {
                Thread.sleep(config.deliveryTime * 1000L);
                log(order.id(), "Delivered by " + config.name);
            }
            System.out.println(config.name + " returned");

        }
    }

    class Dispatcher extends Worker {
        boolean[] cookBusy = new boolean[config.cooks.length];

        @Override
        void loop() throws InterruptedException {
            var order = orderQueue.get();
            while (freeCookQueue.length() > 0) {
                cookBusy[freeCookQueue.get()] = false;
            }
            Config.Cook cc = null;
            int i;
            for (i = 0; i < cookBusy.length; i++) {
                if (!cookBusy[i]) {
                    cc = config.cooks[i];
                    break;
                }
            }
            if (cc == null) {
                var nowFree = freeCookQueue.get();
                cookBusy[nowFree] = false;
                i = nowFree;
                cc = config.cooks[nowFree];
            }
            var worker = new CookWorker(cc, order, pizzaQueue);
            cookBusy[i] = true;
            int finalI = i;
            cookPool.submit(() -> {
                worker.run();
                try {
                    freeCookQueue.put(finalI);
                } catch (InterruptedException ignored) {
                }
            });
        }
    }

}
