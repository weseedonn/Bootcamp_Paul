package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputHelper {

    // Keeps asking until the user types something that is not blank and has no | in it
    public static String promptText(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = scanner.nextLine().trim();

            if (text.isEmpty()) {
                System.out.println("This cannot be blank.");
            } else if (text.contains("|")) {
                // | separates the fields in transactions.csv, so it cannot be part of the text
                System.out.println("Please do not use the | character.");
            } else {
                return text;
            }
        }
    }

    // Keeps asking until the user types a dollar amount between 0.01 and 1,000,000,000
    public static double promptAmount(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().replace("$", "").replace(",", "");

            try {
                double amount = Double.parseDouble(input);

                // Also rejects NaN and Infinity, which parseDouble accepts
                if (amount >= 0.01 && amount <= 1_000_000_000) {
                    return Math.round(amount * 100) / 100.0;
                }

                System.out.println("The amount must be between 0.01 and 1,000,000,000.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number like 25.50.");
            }
        }
    }

    // Returns the text the user typed, or an empty string if they skipped it
    public static String promptOptionalText(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    // Returns the date the user typed, or null if they skipped it
    public static LocalDate promptOptionalDate(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return null;
            }

            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Please use the format yyyy-MM-dd, like 2026-11-04.");
            }
        }
    }

    // Returns the amount the user typed (negative allowed), or null if they skipped it
    public static Double promptOptionalAmount(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().replace("$", "").replace(",", "");

            if (input.isEmpty()) {
                return null;
            }

            try {
                double amount = Double.parseDouble(input);

                // parseDouble also accepts NaN and Infinity, which are not real amounts
                if (Double.isFinite(amount)) {
                    return amount;
                }

                System.out.println("Please enter a number like -25.50.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number like -25.50.");
            }
        }
    }
}