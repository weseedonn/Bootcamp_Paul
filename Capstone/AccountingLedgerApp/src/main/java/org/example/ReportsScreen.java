package org.example;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ReportsScreen {

    public static void show(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("\n=== REPORTS ===");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            LocalDate today = LocalDate.now();

            if (choice.equals("1")) {
                showMonthToDate(today);
            } else if (choice.equals("2")) {
                showPreviousMonth(today);
            } else if (choice.equals("3")) {
                showYearToDate(today);
            } else if (choice.equals("4")) {
                showPreviousYear(today);
            } else if (choice.equals("5")) {
                searchByVendor(scanner);
            } else if (choice.equals("6")) {
                customSearch(scanner);
            } else if (choice.equals("0")) {
                running = false;
            } else {
                System.out.println("Invalid option. Please enter a number from 0 to 6.");
            }
        }
    }

    private static void showMonthToDate(LocalDate today) {
        showDateRange("MONTH TO DATE", today.withDayOfMonth(1), today);
    }

    private static void showPreviousMonth(LocalDate today) {
        LocalDate firstOfThisMonth = today.withDayOfMonth(1);
        LocalDate firstOfLastMonth = firstOfThisMonth.minusMonths(1);
        LocalDate lastOfLastMonth = firstOfThisMonth.minusDays(1);

        showDateRange("PREVIOUS MONTH", firstOfLastMonth, lastOfLastMonth);
    }

    private static void showYearToDate(LocalDate today) {
        showDateRange("YEAR TO DATE", today.withDayOfYear(1), today);
    }

    private static void showPreviousYear(LocalDate today) {
        int lastYear = today.getYear() - 1;

        showDateRange("PREVIOUS YEAR", LocalDate.of(lastYear, 1, 1), LocalDate.of(lastYear, 12, 31));
    }

    // Shows the transactions from startDate to endDate. Both days are included.
    private static void showDateRange(String title, LocalDate startDate, LocalDate endDate) {
        List<Transaction> matches = LedgerScreen.loadNewestFirst().stream()
                .filter(transaction -> !transaction.getDate().isBefore(startDate)
                        && !transaction.getDate().isAfter(endDate))
                .toList();

        LedgerScreen.printTransactions(title + " (" + startDate + " to " + endDate + ")", matches);
    }

    private static void searchByVendor(Scanner scanner) {
        String vendor = InputHelper.promptText(scanner, "Vendor name: ");

        List<Transaction> matches = LedgerScreen.loadNewestFirst().stream()
                .filter(transaction -> containsIgnoreCase(transaction.getVendor(), vendor))
                .toList();

        LedgerScreen.printTransactions("VENDOR: " + vendor, matches);
    }

    // Bonus feature: combine any of the filters. A blank answer skips that filter.
    private static void customSearch(Scanner scanner) {
        System.out.println("\nLeave any field blank to skip it.");
        LocalDate startDate = InputHelper.promptOptionalDate(scanner, "Start date (yyyy-MM-dd): ");
        LocalDate endDate = InputHelper.promptOptionalDate(scanner, "End date (yyyy-MM-dd): ");
        String description = InputHelper.promptOptionalText(scanner, "Description contains: ");
        String vendor = InputHelper.promptOptionalText(scanner, "Vendor contains: ");
        Double amount = InputHelper.promptOptionalAmount(scanner, "Exact amount (payments are negative): ");

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            System.out.println("The start date cannot be after the end date.");
            return;
        }

        List<Transaction> matches = LedgerScreen.loadNewestFirst().stream()
                .filter(transaction -> matchesCustomSearch(
                        transaction, startDate, endDate, description, vendor, amount))
                .toList();

        LedgerScreen.printTransactions("CUSTOM SEARCH", matches);
    }

    // A null date or amount and an empty text mean "do not filter on this"
    private static boolean matchesCustomSearch(Transaction transaction, LocalDate startDate,
                                               LocalDate endDate, String description,
                                               String vendor, Double amount) {
        return (startDate == null || !transaction.getDate().isBefore(startDate))
                && (endDate == null || !transaction.getDate().isAfter(endDate))
                && (description.isEmpty() || containsIgnoreCase(transaction.getDescription(), description))
                && (vendor.isEmpty() || containsIgnoreCase(transaction.getVendor(), vendor))
                && (amount == null || Math.abs(transaction.getAmount() - amount) < 0.005);
    }

    private static boolean containsIgnoreCase(String text, String searchFor) {
        return text.toLowerCase().contains(searchFor.toLowerCase());
    }
}
