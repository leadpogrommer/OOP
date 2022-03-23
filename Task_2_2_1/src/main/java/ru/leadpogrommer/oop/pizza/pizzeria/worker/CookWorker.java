package ru.leadpogrommer.oop.pizza.pizzeria.worker;

import ru.leadpogrommer.oop.pizza.pizzeria.Config;
import ru.leadpogrommer.oop.pizza.pizzeria.Order;
import ru.leadpogrommer.oop.pizza.pizzeria.Pizzeria;
import ru.leadpogrommer.oop.pizza.pizzeria.Queue;

class CookWorker implements Runnable {
    final Config.Cook config;
    final Order order;
    final Queue<Order> storage;

    CookWorker(Config.Cook c, Order order, Queue<Order> storage) {
        this.config = c;
        this.order = order;
        this.storage = storage;
    }

    @Override
    public void run() {
        try {
            Pizzeria.log(order.id(), config.name + " started cooking");
            Thread.sleep(config.cookTime * 1000L);
            Pizzeria.log(order.id(), config.name + " finished cooking");
            storage.put(order);
            Pizzeria.log(order.id(), config.name + " put pizza to storage");
        } catch (InterruptedException ignored) {
        }

    }
}