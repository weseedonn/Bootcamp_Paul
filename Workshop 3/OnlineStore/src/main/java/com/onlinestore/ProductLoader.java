package com.onlinestore;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ProductLoader {

    public static List<Product> loadProducts(String filePath) {
        List<Product> products = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // skip the header row

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                String[] parts = line.split("\\|");
                if (parts.length < 4) {
                    System.out.println("Skipping bad line: " + line);
                    continue;
                }

                try {
                    String sku = parts[0].trim();
                    String name = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());
                    String department = parts[3].trim();
                    products.add(new Product(sku, name, price, department));
                } catch (NumberFormatException e) {
                    System.out.println("Skipping line with a bad price: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read " + filePath + ": " + e.getMessage());
        }

        return products;
    }
}
