package ru.leadpogrommer.oop.pizza.pizzeria.worker;

import ru.leadpogrommer.oop.pizza.pizzeria.Config;
import ru.leadpogrommer.oop.pizza.pizzeria.Order;
import ru.leadpogrommer.oop.pizza.pizzeria.Pizzeria;
import ru.leadpogrommer.oop.pizza.pizzeria.Queue;

public class DeliveryWorker extends Worker {
    final Config.DeliveryMan config;
    final Queue<Order> pizzaQueue;

    public DeliveryWorker(Config.DeliveryMan c, Queue<Order> pizzaQueue) {
        this.config = c;
        this.pizzaQueue = pizzaQueue;
    }

    @Override
    void loop() throws InterruptedException {
        var orders = pizzaQueue.getMany(config.trunkSize);
        System.out.println(config.name + " got pizzas: " + orders.size());
        for (var order : orders) {
            Thread.sleep(config.deliveryTime * 1000L);
            Pizzeria.log(order.id(), "Delivered by " + config.name);
        }
        System.out.println(config.name + " returned");

    }
}