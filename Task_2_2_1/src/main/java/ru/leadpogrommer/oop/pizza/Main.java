package ru.leadpogrommer.oop.pizza;

import com.google.gson.Gson;
import ru.leadpogrommer.oop.pizza.pizzeria.Config;
import ru.leadpogrommer.oop.pizza.pizzeria.Pizzeria;
import sun.misc.Signal;

import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws InterruptedException {
        var gson = new Gson();
        var configStream = Main.class.getClassLoader().getResourceAsStream("config.json");
        assert configStream != null;
        var config = gson.fromJson(new InputStreamReader(configStream), Config.class);

        var pizzeria = new Pizzeria(config);
        var scanner = new Scanner(System.in);

        System.out.println("Enter min delta:");
        var minTime = scanner.nextInt();
        System.out.println("Enter max delta:");
        var maxTime = scanner.nextInt();
        System.out.println("Enter amount:");


        var running = new Object() {
            boolean value = true;
        };

        Signal.handle(new Signal("INT"), (i) -> {
            running.value = false;
        });
        var rng = new Random();

        while (running.value) {
            var delayTime = rng.nextInt(minTime, maxTime + 1);
            Thread.sleep(delayTime);
            pizzeria.placeOrder("");
        }
        pizzeria.stop();
    }
}
