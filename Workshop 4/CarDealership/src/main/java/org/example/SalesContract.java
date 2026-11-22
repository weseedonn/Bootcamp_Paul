package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

// A contract for buying a vehicle
public class SalesContract extends Contract {

    // Change these numbers here if the rules change
    private static final BigDecimal SALES_TAX_RATE = new BigDecimal("0.05");
    private static final BigDecimal RECORDING_FEE = new BigDecimal("100.00");
    private static final BigDecimal LOW_PROCESSING_FEE = new BigDecimal("295.00");
    private static final BigDecimal HIGH_PROCESSING_FEE = new BigDecimal("495.00");
    private static final BigDecimal PRICE_LIMIT = new BigDecimal("10000");

    private final boolean financed;

    public SalesContract(LocalDate date, String customerName, String customerEmail,
                         Vehicle vehicleSold, boolean financed) {
        super(date, customerName, customerEmail, vehicleSold);
        this.financed = financed;
    }

    public boolean isFinanced() {
        return financed;
    }

    public BigDecimal getSalesTax() {
        return getVehicleSold().getPrice().multiply(SALES_TAX_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getRecordingFee() {
        return RECORDING_FEE;
    }

    // $295 for vehicles under $10,000, otherwise $495
    public BigDecimal getProcessingFee() {
        if (getVehicleSold().getPrice().compareTo(PRICE_LIMIT) < 0) {
            return LOW_PROCESSING_FEE;
        }

        return HIGH_PROCESSING_FEE;
    }

    @Override
    public BigDecimal getTotalPrice() {
        return getVehicleSold().getPrice()
                .add(getSalesTax())
                .add(getRecordingFee())
                .add(getProcessingFee())
                .setScale(2, RoundingMode.HALF_UP);
    }

    // No loan means no monthly payment.
    // Loans: 4.25% for 48 months when the price is $10,000 or more, otherwise 5.25% for 24 months.
    @Override
    public BigDecimal getMonthlyPayment() {
        if (!financed) {
            return BigDecimal.ZERO.setScale(2);
        }

        if (getVehicleSold().getPrice().compareTo(PRICE_LIMIT) >= 0) {
            return calculateMonthlyPayment(getTotalPrice(), 4.25, 48);
        }

        return calculateMonthlyPayment(getTotalPrice(), 5.25, 24);
    }
}
