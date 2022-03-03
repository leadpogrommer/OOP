package ru.leadpogrommer.oop.pizza;

import com.google.gson.Gson;

import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        var gson = new Gson();
        var configStream = Main.class.getClassLoader().getResourceAsStream("config.json");
        assert configStream != null;
        var config = gson.fromJson(new InputStreamReader(configStream), Config.class);

        var pizzeria = new Pizzeria(config);

        boolean running = true;
        while (running) {
            System.out.println("Type address and we will deliver pizza:");
            var data = System.console().readLine();
            if (data.equals("stop")) {
                running = false;
            } else {
                pizzeria.placeOrder(data);
            }
        }
        pizzeria.stop();
    }
}
