package com.onlinestore;

public class Main {

    public static void main(String[] args) {
        Store store = new Store("products.csv");
        store.run();
    }
}
