package org.example;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            showHomeScreen(scanner);
        }
    }

    private static void showHomeScreen(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("\n=== ACCOUNTING LEDGER ===");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            if (choice.equalsIgnoreCase("D")) {
                recordTransaction(scanner, true);
            } else if (choice.equalsIgnoreCase("P")) {
                recordTransaction(scanner, false);
            } else if (choice.equalsIgnoreCase("L")) {
                LedgerScreen.show(scanner);
            } else if (choice.equalsIgnoreCase("X")) {
                System.out.println("Goodbye!");
                running = false;
            } else {
                System.out.println("Invalid option. Please enter D, P, L, or X.");
            }
        }
    }

    // Deposits and payments are the same except payments are saved as negative amounts
    private static void recordTransaction(Scanner scanner, boolean isDeposit) {
        String type = isDeposit ? "deposit" : "payment";

        System.out.println("\n--- New " + type + " ---");
        String description = InputHelper.promptText(scanner, "Description: ");
        String vendor = InputHelper.promptText(scanner, "Vendor: ");
        double amount = InputHelper.promptAmount(scanner, "Amount: $");

        if (!isDeposit) {
            amount = -amount;
        }

        Transaction transaction = new Transaction(
                LocalDate.now(), LocalTime.now().withNano(0), description, vendor, amount);

        if (TransactionFileManager.saveTransaction(transaction)) {
            System.out.println("Your " + type + " was saved.");
        }
    }
}
