package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

// A contract for leasing a vehicle
public class LeaseContract extends Contract {

    // Change these numbers here if the rules change
    private static final BigDecimal ENDING_VALUE_RATE = new BigDecimal("0.50");
    private static final BigDecimal LEASE_FEE_RATE = new BigDecimal("0.07");
    private static final double INTEREST_RATE_PERCENT = 4.0;
    private static final int LEASE_MONTHS = 36;
    private static final int MAX_VEHICLE_AGE_YEARS = 3;

    public LeaseContract(LocalDate date, String customerName, String customerEmail, Vehicle vehicleSold) {
        super(date, customerName, customerEmail, vehicleSold);
    }

    // Only vehicles that are 3 years old or newer can be leased
    public static boolean canLease(Vehicle vehicle) {
        return vehicle.getYear() >= LocalDate.now().getYear() - MAX_VEHICLE_AGE_YEARS;
    }

    public static int getLeaseMonths() {
        return LEASE_MONTHS;
    }

    // What the vehicle is expected to be worth when the lease ends: 50% of the price
    public BigDecimal getExpectedEndingValue() {
        return getVehicleSold().getPrice().multiply(ENDING_VALUE_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    // 7% of the price
    public BigDecimal getLeaseFee() {
        return getVehicleSold().getPrice().multiply(LEASE_FEE_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    // The customer pays for the value the car loses (price - ending value) plus the lease fee
    private BigDecimal getAmountToPayOff() {
        return getVehicleSold().getPrice().subtract(getExpectedEndingValue()).add(getLeaseFee());
    }

    // 4.0% interest over 36 months
    @Override
    public BigDecimal getMonthlyPayment() {
        return calculateMonthlyPayment(getAmountToPayOff(), INTEREST_RATE_PERCENT, LEASE_MONTHS);
    }

    // Everything the customer pays over the whole lease
    @Override
    public BigDecimal getTotalPrice() {
        return getMonthlyPayment().multiply(BigDecimal.valueOf(LEASE_MONTHS)).setScale(2, RoundingMode.HALF_UP);
    }
}
