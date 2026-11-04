package org.example;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class LedgerScreen {

    public static void show(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("\n=== LEDGER ===");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            if (choice.equalsIgnoreCase("A")) {
                displayAll();
            } else if (choice.equalsIgnoreCase("D")) {
                displayDeposits();
            } else if (choice.equalsIgnoreCase("P")) {
                displayPayments();
            } else if (choice.equalsIgnoreCase("R")) {
                ReportsScreen.show(scanner);
            } else if (choice.equalsIgnoreCase("H")) {
                running = false;
            } else {
                System.out.println("Invalid option. Please enter A, D, P, R, or H.");
            }
        }
    }

    private static void displayAll() {
        printTransactions("ALL TRANSACTIONS", loadNewestFirst());
    }

    private static void displayDeposits() {
        List<Transaction> deposits = loadNewestFirst().stream()
                .filter(Transaction::isDeposit)
                .toList();

        printTransactions("DEPOSITS", deposits);
    }

    private static void displayPayments() {
        List<Transaction> payments = loadNewestFirst().stream()
                .filter(Transaction::isPayment)
                .toList();

        printTransactions("PAYMENTS", payments);
    }

    // The file is in the order entries were added, so we sort by date and time ourselves
    public static List<Transaction> loadNewestFirst() {
        List<Transaction> transactions = TransactionFileManager.loadTransactions();

        transactions.sort(Comparator.comparing(Transaction::getDate)
                .thenComparing(Transaction::getTime)
                .reversed());

        return transactions;
    }

    public static void printTransactions(String title, List<Transaction> transactions) {
        System.out.println("\n--- " + title + " ---");

        if (transactions.isEmpty()) {
            System.out.println("No transactions to show.");
            return;
        }

        System.out.println(String.format("%-10s  %-8s  %-28s  %-16s  %12s",
                "Date", "Time", "Description", "Vendor", "Amount"));

        for (Transaction transaction : transactions) {
            System.out.println(String.format("%-10s  %-8s  %-28s  %-16s  %12.2f",
                    transaction.getDate(),
                    transaction.getFormattedTime(),
                    transaction.getDescription(),
                    transaction.getVendor(),
                    transaction.getAmount()));
        }
    }
}