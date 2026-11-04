package org.example;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class Transaction {

    // LocalTime.toString() drops the seconds when they are 00, so we format them ourselves
    private static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final LocalDate date;
    private final LocalTime time;
    private final String description;
    private final String vendor;
    private final double amount;

    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getFormattedTime() {
        return time.format(timeFormat);
    }

    public LocalTime getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isDeposit() {
        return amount > 0;
    }

    public boolean isPayment() {
        return amount < 0;
    }

    // Builds the line saved in transactions.csv: date|time|description|vendor|amount
    public String toCsvLine() {
        return date + "|"
                + time.format(timeFormat) + "|"
                + description + "|"
                + vendor + "|"
                + String.format(Locale.US, "%.2f", amount);
    }

    // Turns one line from transactions.csv back into a Transaction
    public static Transaction fromCsvLine(String line) {
        // "\\|" because | is a special character in regular expressions
        String[] parts = line.split("\\|");

        if (parts.length != 5) {
            throw new IllegalArgumentException("Expected 5 fields but found " + parts.length + ": " + line);
        }

        try {
            return new Transaction(
                    LocalDate.parse(parts[0]),
                    LocalTime.parse(parts[1]),
                    parts[2],
                    parts[3],
                    Double.parseDouble(parts[4]));
        } catch (DateTimeParseException | NumberFormatException e) {
            throw new IllegalArgumentException("Could not read line: " + line, e);
        }
    }
}
