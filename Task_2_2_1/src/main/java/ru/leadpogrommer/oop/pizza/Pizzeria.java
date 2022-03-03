package ru.leadpogrommer.oop.pizza;

public class Pizzeria {
    private final OrderQueue<Order> orderQueue;
    private final OrderQueue<Order> pizzaQueue;
    private final Thread[] workers;
    int nextId = 0;

    Pizzeria(Config config) {
        workers = new Thread[config.cooks.length + config.deliveryMen.length];
        orderQueue = new OrderQueue<Order>(100);
        pizzaQueue = new OrderQueue<Order>(config.storageSize);
        int i = 0;
        for (var cc : config.cooks) {
            workers[i] = new Thread(new CookWorker(cc));
            workers[i].setDaemon(true);
            workers[i++].start();
        }
        for (var dc : config.deliveryMen) {
            workers[i] = new Thread(new DeliveryWorker(dc));
            workers[i].setDaemon(true);
            workers[i++].start();
        }
    }

    static void log(int orderId, String message) {
        System.out.printf("[%d] %s%n", orderId, message);
    }

    void placeOrder(String address) throws InterruptedException {
        log(nextId, "Accepted");
        orderQueue.put(new Order(nextId++, address));
    }

    void stop() {
        for (var worker : workers) {
            worker.interrupt();
        }
    }

    class CookWorker extends Worker {
        final Config.Cook config;

        CookWorker(Config.Cook c) {
            this.config = c;
        }

        @Override
        void loop() throws InterruptedException {
            var order = orderQueue.get();
            log(order.id(), config.name + " started cooking");
            Thread.sleep(config.cookTime * 1000L);
            log(order.id(), config.name + " finished cooking");
            pizzaQueue.put(order);
            log(order.id(), config.name + " put pizza to storage");
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


}
