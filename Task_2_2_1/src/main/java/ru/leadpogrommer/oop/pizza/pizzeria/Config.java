package ru.leadpogrommer.oop.pizza.pizzeria;

public class Config {
    public int storageSize;
    public Cook[] cooks;
    public DeliveryMan[] deliveryMen;
    public int threadCount;

    public static class Cook {
        public String name;
        public int cookTime;
    }

    public static class DeliveryMan {
        public String name;
        public int deliveryTime;
        public int trunkSize;
    }
}
