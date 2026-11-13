package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class DealershipFileManager {

    private static final String fileName = "src/main/resources/inventory.csv";

    // Reads inventory.csv and builds a Dealership full of Vehicles. Returns null if the file cannot be used.
    public Dealership getDealership() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            // The first line of the file is the dealership itself
            Dealership dealership = parseDealership(reader.readLine());

            if (dealership == null) {
                System.out.println("The first line of " + fileName + " is not a valid dealership.");
                return null;
            }

            // Every line after that is one vehicle
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                try {
                    dealership.addVehicle(parseVehicle(line));
                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping unreadable line: " + line);
                }
            }

            return dealership;
        } catch (IOException e) {
            System.out.println("Could not read " + fileName + ": " + e.getMessage());
            return null;
        }
    }

    // Rewrites inventory.csv with the dealership on the first line and one vehicle per line after it
    public void saveDealership(Dealership dealership) {
        Path file = Path.of(fileName);
        Path tempFile = Path.of(fileName + ".tmp");

        try {
            // Write to a temporary file first, so a failure halfway through cannot wipe out the real inventory
            writeInventory(dealership, tempFile);
            Files.move(tempFile, file, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Could not save the inventory: " + e.getMessage());
        }
    }

    private void writeInventory(Dealership dealership, Path path) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()))) {
            writer.write(dealership.getName() + "|" + dealership.getAddress() + "|" + dealership.getPhone());
            writer.newLine();

            for (Vehicle vehicle : dealership.getAllVehicles()) {
                writer.write(toCsvLine(vehicle));
                writer.newLine();
            }
        }
    }

    // "name|address|phone" -> Dealership, or null if the line is missing or incomplete
    private Dealership parseDealership(String line) {
        if (line == null) {
            return null;
        }

        // "\\|" because | is a special character in regular expressions
        String[] parts = line.split("\\|");

        if (parts.length != 3) {
            return null;
        }

        return new Dealership(parts[0], parts[1], parts[2]);
    }

    // "vin|year|make|model|type|color|odometer|price" -> Vehicle. A bad line throws IllegalArgumentException.
    private Vehicle parseVehicle(String line) {
        String[] parts = line.split("\\|");

        if (parts.length != 8) {
            throw new IllegalArgumentException("Expected 8 fields but found " + parts.length);
        }

        // NumberFormatException is a kind of IllegalArgumentException, so a bad number is caught too
        return new Vehicle(
                Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                parts[2],
                parts[3],
                parts[4],
                parts[5],
                Integer.parseInt(parts[6]),
                new BigDecimal(parts[7]));
    }

    // The reverse of parseVehicle. Prices always get two decimals, like 6995.00
    private String toCsvLine(Vehicle vehicle) {
        return vehicle.getVin() + "|"
                + vehicle.getYear() + "|"
                + vehicle.getMake() + "|"
                + vehicle.getModel() + "|"
                + vehicle.getVehicleType() + "|"
                + vehicle.getColor() + "|"
                + vehicle.getOdometer() + "|"
                + vehicle.getPrice().setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}