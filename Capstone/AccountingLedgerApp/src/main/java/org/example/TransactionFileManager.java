package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class TransactionFileManager {

    private static final String fileName = "src/main/resources/transactions.csv";
    private static final String header = "date|time|description|vendor|amount";

    // Reads every valid transaction from the file. Bad lines are skipped, not fatal.
    public static List<Transaction> loadTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        if (!new File(fileName).exists()) {
            return transactions;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank() || line.equals(header)) {
                    continue;
                }

                try {
                    transactions.add(Transaction.fromCsvLine(line));
                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping unreadable line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read " + fileName + ": " + e.getMessage());
        }

        return transactions;
    }

    // Adds one transaction to the end of the file. Returns true if it was saved.
    public static boolean saveTransaction(Transaction transaction) {
        File file = new File(fileName);
        boolean needsHeader = !file.exists();

        // true = append to the end instead of overwriting the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (needsHeader) {
                writer.write(header);
                writer.newLine();
            } else if (!endsWithNewLine(file)) {
                // Without this, the new entry would be glued onto the end of the last line
                writer.newLine();
            }

            writer.write(transaction.toCsvLine());
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Could not save the transaction: " + e.getMessage());
            return false;
        }
    }


    private static boolean endsWithNewLine(@org.jetbrains.annotations.NotNull File file) throws IOException {
        String content = Files.readString(file.toPath());
        return content.isEmpty() || content.endsWith("\n");
    }
}