package ru.leadpogrommer.oop.pizza;

import com.google.gson.Gson;
import ru.leadpogrommer.oop.pizza.pizzeria.Config;
import ru.leadpogrommer.oop.pizza.pizzeria.Pizzeria;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws InterruptedException {
        var gson = new Gson();
        var configStream = Main.class.getClassLoader().getResourceAsStream("config.json");
        assert configStream != null;
        var config = gson.fromJson(new InputStreamReader(configStream), Config.class);

        var scanner = new Scanner(System.in);

        System.out.println("Enter min delta:");
        var minTime = scanner.nextInt();
        System.out.println("Enter max delta:");
        var maxTime = scanner.nextInt();
        System.out.println("Enter amount:");
        var count = scanner.nextInt();

        var deliveredOrders = new Boolean[count];
        Arrays.fill(deliveredOrders, false);

        var pizzeria = new Pizzeria(config, (order) -> {
            synchronized (deliveredOrders) {
                deliveredOrders[order] = true;
                deliveredOrders.notifyAll();
            }
            return null;
        });

        var rng = new Random();

        for (int i = 0; i < count; i++) {
            var delayTime = rng.nextInt(minTime, maxTime + 1);
            Thread.sleep(delayTime);
            pizzeria.placeOrder("delivery to " + i);
        }

        System.out.println("Stooped generating orders");
        while (true) {
            synchronized (deliveredOrders) {
                if (Arrays.stream(deliveredOrders).allMatch((t) -> t)) {
                    break;
                }
                deliveredOrders.wait();
            }
        }

        System.out.println("delivered all orders");

        pizzeria.stop();
    }
}
