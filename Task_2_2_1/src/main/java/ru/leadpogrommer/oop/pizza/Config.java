package ru.leadpogrommer.oop.pizza;

public class Config {
    public int storageSize;
    public Cook[] cooks;
    public DeliveryMan[] deliveryMen;
    public int threadCount;

    static class Cook {
        String name;
        int cookTime;
    }

    static class DeliveryMan {
        String name;
        int deliveryTime;
        int trunkSize;
    }
}
