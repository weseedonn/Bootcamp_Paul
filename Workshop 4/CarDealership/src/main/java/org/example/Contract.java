package org.example;

import java.math.BigDecimal;
import java.time.LocalDate;

// The parts every contract has in common. SalesContract and LeaseContract fill in the prices.
public abstract class Contract {

    private final LocalDate date;
    private final String customerName;
    private final String customerEmail;
    private final Vehicle vehicleSold;

    public Contract(LocalDate date, String customerName, String customerEmail, Vehicle vehicleSold) {
        this.date = date;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.vehicleSold = vehicleSold;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public Vehicle getVehicleSold() {
        return vehicleSold;
    }

    // Each type of contract works out its own total and monthly payment
    public abstract BigDecimal getTotalPrice();

    public abstract BigDecimal getMonthlyPayment();

    // Standard loan formula: payment = amount * r / (1 - (1 + r)^-months), where r is the monthly rate
    protected static BigDecimal calculateMonthlyPayment(BigDecimal amount, double annualRatePercent, int months) {
        double monthlyRate = annualRatePercent / 100 / 12;
        double payment = amount.doubleValue() * monthlyRate / (1 - Math.pow(1 + monthlyRate, -months));

        return BigDecimal.valueOf(payment).setScale(2, java.math.RoundingMode.HALF_UP);
    }
}
