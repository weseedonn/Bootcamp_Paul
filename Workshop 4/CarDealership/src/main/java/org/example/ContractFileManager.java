package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

// Saves finished contracts to contracts.csv, one line per contract
public class ContractFileManager {

    private static final String fileName = "src/main/resources/contracts.csv";

    // Adds one line to the end of the file. Returns true if it was saved.
    public boolean saveContract(Contract contract) {
        try {
            Files.createDirectories(Path.of(fileName).getParent());

            // true = add to the end of the file instead of replacing what is already there
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
                writer.write(toCsvLine(contract));
                writer.newLine();
            }

            return true;
        } catch (IOException e) {
            System.out.println("Could not save the contract: " + e.getMessage());
            return false;
        }
    }

    // Starts with SALE or LEASE, then the shared fields, then the fields only that type has
    private String toCsvLine(Contract contract) {
        Vehicle vehicle = contract.getVehicleSold();
        String date = contract.getDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String sharedPart = date + "|"
                + contract.getCustomerName() + "|"
                + contract.getCustomerEmail() + "|"
                + vehicle.getVin() + "|"
                + vehicle.getYear() + "|"
                + vehicle.getMake() + "|"
                + vehicle.getModel() + "|"
                + vehicle.getVehicleType() + "|"
                + vehicle.getColor() + "|"
                + vehicle.getOdometer() + "|"
                + money(vehicle.getPrice());

        if (contract instanceof SalesContract sale) {
            return "SALE|" + sharedPart + "|"
                    + money(sale.getSalesTax()) + "|"
                    + money(sale.getRecordingFee()) + "|"
                    + money(sale.getProcessingFee()) + "|"
                    + money(sale.getTotalPrice()) + "|"
                    + (sale.isFinanced() ? "YES" : "NO") + "|"
                    + money(sale.getMonthlyPayment());
        }

        LeaseContract lease = (LeaseContract) contract;

        return "LEASE|" + sharedPart + "|"
                + money(lease.getExpectedEndingValue()) + "|"
                + money(lease.getLeaseFee()) + "|"
                + money(lease.getTotalPrice()) + "|"
                + money(lease.getMonthlyPayment());
    }

    // Money always gets two decimals, like 6995.00
    private String money(java.math.BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}
