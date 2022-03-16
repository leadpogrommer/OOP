package ru.leadpogrommer.oop.pizza.pizzeria.worker;

import ru.leadpogrommer.oop.pizza.pizzeria.Config;
import ru.leadpogrommer.oop.pizza.pizzeria.Order;
import ru.leadpogrommer.oop.pizza.pizzeria.Queue;

import java.util.concurrent.ThreadPoolExecutor;

public class DispatcherWorker extends Worker {
    private final Queue<Integer> freeCookQueue;
    private final Queue<Order> orderQueue;
    private final Queue<Order> pizzaQueue;
    private final Config config;
    private final ThreadPoolExecutor cookPool;
    boolean[] cookBusy;


    public DispatcherWorker(Config config, Queue<Order> orderQueue, Queue<Order> pizzaQueue, ThreadPoolExecutor cookPool) {
        this.freeCookQueue = new Queue<>(100);
        this.config = config;
        this.orderQueue = orderQueue;
        this.pizzaQueue = pizzaQueue;
        this.cookBusy = new boolean[config.cooks.length];
        this.cookPool = cookPool;
    }

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
