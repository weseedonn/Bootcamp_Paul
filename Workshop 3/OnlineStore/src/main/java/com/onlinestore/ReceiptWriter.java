package com.onlinestore;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ReceiptWriter {

    private static final String RECEIPTS_FOLDER = "Receipts";


    public static String buildReceipt(Cart cart, double amountPaid, double change, LocalDateTime orderDate) {
        DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder receipt = new StringBuilder();

        receipt.append("==================== SALES RECEIPT ====================\n");
        receipt.append("Order Date: ").append(orderDate.format(displayFormat)).append("\n");
        receipt.append("-------------------------------------------------------\n");

        for (CartItem item : cart.getItems()) {
            receipt.append(item).append("\n");
        }

        receipt.append("-------------------------------------------------------\n");
        receipt.append(String.format("Sales Total:  $%.2f%n", cart.getTotal()));
        receipt.append(String.format("Amount Paid:  $%.2f%n", amountPaid));
        receipt.append(String.format("Change Given: $%.2f%n", change));
        receipt.append("=======================================================\n");

        return receipt.toString();
    }


    public static String saveReceipt(String receiptText, LocalDateTime orderDate) {
        String fileName = orderDate.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + ".txt";
        Path filePath = Path.of(RECEIPTS_FOLDER, fileName);

        try {
            Files.createDirectories(Path.of(RECEIPTS_FOLDER));
            try (FileWriter writer = new FileWriter(filePath.toFile())) {
                writer.write(receiptText);
            }
            return filePath.toString();
        } catch (IOException e) {
            System.out.println("Could not save receipt file: " + e.getMessage());
            return null;
        }
    }
}
