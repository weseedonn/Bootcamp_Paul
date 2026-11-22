package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class UserInterface {

    private static final String[] vehicleTypes = {"car", "truck", "SUV", "van"};

    private Dealership dealership;
    private DealershipFileManager fileManager;
    private final ContractFileManager contractFileManager = new ContractFileManager();
    private final Scanner scanner = new Scanner(System.in);

    public UserInterface() {
    }

    public void display() {
        init();

        if (dealership == null) {
            System.out.println("The dealership could not be loaded, so the application cannot start.");
            return;
        }

        boolean running = true;

        while (running) {
            displayMenu();

            switch (readCommand()) {
                case 1 -> processGetByPriceRequest();
                case 2 -> processGetByMakeModelRequest();
                case 3 -> processGetByYearRequest();
                case 4 -> processGetByColorRequest();
                case 5 -> processGetByMileageRequest();
                case 6 -> processGetByVehicleTypeRequest();
                case 7 -> processGetAllVehiclesRequest();
                case 8 -> processAddVehicleRequest();
                case 9 -> processRemoveVehicleRequest();
                case 10 -> processBuyRequest();
                case 11 -> processLeaseRequest();
                case 99 -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option. Please enter a number from the menu.");
            }
        }

        scanner.close();
    }

    // Loads the dealership from the inventory file
    private void init() {
        this.fileManager = new DealershipFileManager();
        this.dealership = fileManager.getDealership();
    }

    private void displayMenu() {
        System.out.println("\n=== " + dealership.getName() + " ===");
        System.out.println(" 1 - Find vehicles within a price range");
        System.out.println(" 2 - Find vehicles by make / model");
        System.out.println(" 3 - Find vehicles by year range");
        System.out.println(" 4 - Find vehicles by color");
        System.out.println(" 5 - Find vehicles by mileage range");
        System.out.println(" 6 - Find vehicles by type (car, truck, SUV, van)");
        System.out.println(" 7 - List ALL vehicles");
        System.out.println(" 8 - Add a vehicle");
        System.out.println(" 9 - Remove a vehicle");
        System.out.println("10 - Buy a vehicle");
        System.out.println("11 - Lease a vehicle");
        System.out.println("99 - Quit");
        System.out.print("Enter your choice: ");
    }

    // Reads one line and turns it into a number. Returns -1 if it is not a number.
    private int readCommand() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Shows any list of vehicles, so every search can reuse it
    private void displayVehicles(List<Vehicle> vehicles) {
        if (vehicles == null || vehicles.isEmpty()) {
            System.out.println("No vehicles found.");
            return;
        }

        System.out.printf("%n%-8s %-5s %-11s %-18s %-6s %-8s %9s  %11s%n",
                "VIN", "Year", "Make", "Model", "Type", "Color", "Odometer", "Price");

        for (Vehicle vehicle : vehicles) {
            // Build the price text first (like $6,995.00) so the $ sits right next to the number
            String price = String.format("$%,.2f", vehicle.getPrice());

            System.out.printf("%-8d %-5d %-11s %-18s %-6s %-8s %9d  %11s%n",
                    vehicle.getVin(),
                    vehicle.getYear(),
                    vehicle.getMake(),
                    vehicle.getModel(),
                    vehicle.getVehicleType(),
                    vehicle.getColor(),
                    vehicle.getOdometer(),
                    price);
        }
    }

    public void processGetByPriceRequest() {
        BigDecimal min = promptForPrice("Minimum price: $");
        BigDecimal max = promptForPrice("Maximum price: $");

        if (min.compareTo(max) > 0) {
            System.out.println("The minimum cannot be higher than the maximum.");
            return;
        }

        displayVehicles(dealership.getVehiclesByPrice(min, max));
    }

    public void processGetByMakeModelRequest() {
        String make = promptForText("Make: ");
        String model = promptForText("Model: ");

        displayVehicles(dealership.getVehiclesByMakeModel(make, model));
    }

    public void processGetByYearRequest() {
        int min = promptForWholeNumber("Minimum year: ");
        int max = promptForWholeNumber("Maximum year: ");

        if (min > max) {
            System.out.println("The minimum cannot be higher than the maximum.");
            return;
        }

        displayVehicles(dealership.getVehiclesByYear(min, max));
    }

    public void processGetByColorRequest() {
        String color = promptForText("Color: ");

        displayVehicles(dealership.getVehiclesByColor(color));
    }

    public void processGetByMileageRequest() {
        int min = promptForWholeNumber("Minimum mileage: ");
        int max = promptForWholeNumber("Maximum mileage: ");

        if (min > max) {
            System.out.println("The minimum cannot be higher than the maximum.");
            return;
        }

        displayVehicles(dealership.getVehiclesByMileage(min, max));
    }

    public void processGetByVehicleTypeRequest() {
        String vehicleType = promptForText("Type (car, truck, SUV, or van): ");

        displayVehicles(dealership.getVehiclesByType(vehicleType));
    }

    public void processGetAllVehiclesRequest() {
        displayVehicles(dealership.getAllVehicles());
    }

    public void processAddVehicleRequest() {
        int vin = promptForNewVin();
        int year = promptForYear();
        String make = promptForText("Make: ");
        String model = promptForText("Model: ");
        String vehicleType = promptForVehicleType();
        String color = promptForText("Color: ");
        int odometer = promptForWholeNumber("Odometer (miles): ");
        BigDecimal price = promptForPrice("Price: $");

        dealership.addVehicle(new Vehicle(vin, year, make, model, vehicleType, color, odometer, price));
        fileManager.saveDealership(dealership);

        System.out.println("Vehicle added.");
    }

    public void processRemoveVehicleRequest() {
        int vin = promptForWholeNumber("VIN of the vehicle to remove: ");
        Vehicle vehicle = findVehicleByVin(vin);

        if (vehicle == null) {
            System.out.println("No vehicle with that VIN was found.");
            return;
        }

        displayVehicles(List.of(vehicle));
        System.out.print("Remove this vehicle? (y/n): ");

        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            dealership.removeVehicle(vehicle);
            fileManager.saveDealership(dealership);
            System.out.println("Vehicle removed.");
        } else {
            System.out.println("Nothing was removed.");
        }
    }

    // Buying a vehicle: collect the customer's info, show the contract, then finish the sale
    public void processBuyRequest() {
        Vehicle vehicle = promptForVehicleForContract("buy");

        if (vehicle == null) {
            return;
        }

        String customerName = promptForText("Customer name: ");
        String customerEmail = promptForEmail();
        boolean financed = promptForYesNo("Finance this purchase? (y/n): ");

        SalesContract contract = new SalesContract(LocalDate.now(), customerName, customerEmail, vehicle, financed);
        displaySalesContract(contract);

        completeContract(contract, "Complete this sale? (y/n): ", "Sale complete! The vehicle was removed from inventory.");
    }

    // Leasing a vehicle: same steps as buying, but only newer vehicles can be leased
    public void processLeaseRequest() {
        Vehicle vehicle = promptForVehicleForContract("lease");

        if (vehicle == null) {
            return;
        }

        if (!LeaseContract.canLease(vehicle)) {
            System.out.println("Only vehicles that are 3 years old or newer can be leased.");
            return;
        }

        String customerName = promptForText("Customer name: ");
        String customerEmail = promptForEmail();

        LeaseContract contract = new LeaseContract(LocalDate.now(), customerName, customerEmail, vehicle);
        displayLeaseContract(contract);

        completeContract(contract, "Complete this lease? (y/n): ", "Lease complete! The vehicle was removed from inventory.");
    }

    // Asks for a VIN and shows the vehicle. Returns null if it cannot be found.
    private Vehicle promptForVehicleForContract(String action) {
        int vin = promptForWholeNumber("VIN of the vehicle to " + action + ": ");
        Vehicle vehicle = findVehicleByVin(vin);

        if (vehicle == null) {
            System.out.println("No vehicle with that VIN was found.");
            return null;
        }

        displayVehicles(List.of(vehicle));
        return vehicle;
    }

    // Asks for a final yes. If yes: saves the contract, then removes the vehicle from inventory.
    private void completeContract(Contract contract, String confirmPrompt, String successMessage) {
        if (!promptForYesNo(confirmPrompt)) {
            System.out.println("Cancelled. Nothing was changed.");
            return;
        }

        // Save the contract first, so a vehicle is never removed without a record of the deal
        if (!contractFileManager.saveContract(contract)) {
            System.out.println("The contract could not be saved, so the vehicle was not removed.");
            return;
        }

        dealership.removeVehicle(contract.getVehicleSold());
        fileManager.saveDealership(dealership);

        System.out.println(successMessage);
    }

    private void displaySalesContract(SalesContract contract) {
        System.out.println("\n========== SALES CONTRACT ==========");
        printContractHeader(contract);
        System.out.printf("Vehicle Price:   %12s%n", formatMoney(contract.getVehicleSold().getPrice()));
        System.out.printf("Sales Tax (5%%):  %12s%n", formatMoney(contract.getSalesTax()));
        System.out.printf("Recording Fee:   %12s%n", formatMoney(contract.getRecordingFee()));
        System.out.printf("Processing Fee:  %12s%n", formatMoney(contract.getProcessingFee()));
        System.out.println("------------------------------------");
        System.out.printf("Total Price:     %12s%n", formatMoney(contract.getTotalPrice()));
        System.out.println("Financed:        " + (contract.isFinanced() ? "Yes" : "No"));

        if (contract.isFinanced()) {
            System.out.printf("Monthly Payment: %12s%n", formatMoney(contract.getMonthlyPayment()));
        }

        System.out.println("====================================");
    }

    private void displayLeaseContract(LeaseContract contract) {
        System.out.println("\n========== LEASE CONTRACT ==========");
        printContractHeader(contract);
        System.out.printf("Vehicle Price:        %12s%n", formatMoney(contract.getVehicleSold().getPrice()));
        System.out.printf("Expected End Value:   %12s%n", formatMoney(contract.getExpectedEndingValue()));
        System.out.printf("Lease Fee (7%%):       %12s%n", formatMoney(contract.getLeaseFee()));
        System.out.println("Lease Length:         " + LeaseContract.getLeaseMonths() + " months");
        System.out.println("------------------------------------");
        System.out.printf("Monthly Payment:      %12s%n", formatMoney(contract.getMonthlyPayment()));
        System.out.printf("Total Over Lease:     %12s%n", formatMoney(contract.getTotalPrice()));
        System.out.println("====================================");
    }

    // The lines that look the same on every contract
    private void printContractHeader(Contract contract) {
        Vehicle vehicle = contract.getVehicleSold();

        System.out.println("Date:     " + contract.getDate());
        System.out.println("Customer: " + contract.getCustomerName());
        System.out.println("Email:    " + contract.getCustomerEmail());
        System.out.println("Vehicle:  " + vehicle.getYear() + " " + vehicle.getMake() + " " + vehicle.getModel()
                + " (VIN " + vehicle.getVin() + ")");
        System.out.println("------------------------------------");
    }

    private String formatMoney(BigDecimal amount) {
        return String.format("$%,.2f", amount);
    }

    // Returns the vehicle with this VIN, or null if there is none
    private Vehicle findVehicleByVin(int vin) {
        for (Vehicle vehicle : dealership.getAllVehicles()) {
            if (vehicle.getVin() == vin) {
                return vehicle;
            }
        }

        return null;
    }

    // The prompt methods below keep asking until the user types something valid

    private String promptForText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = scanner.nextLine().trim();

            if (text.isEmpty()) {
                System.out.println("This cannot be blank.");
            } else if (text.contains("|")) {
                // | separates the fields in inventory.csv, so it cannot be part of the text
                System.out.println("Please do not use the | character.");
            } else {
                return text;
            }
        }
    }

    private int promptForWholeNumber(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                int number = Integer.parseInt(scanner.nextLine().trim().replace(",", ""));

                if (number >= 0) {
                    return number;
                }
            } catch (NumberFormatException e) {
                // Falls through to the message below
            }

            System.out.println("Please enter a whole number, like 2015.");
        }
    }

    private BigDecimal promptForPrice(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                BigDecimal price = new BigDecimal(scanner.nextLine().trim().replace("$", "").replace(",", ""));

                if (price.signum() >= 0) {
                    return price.setScale(2, RoundingMode.HALF_UP);
                }
            } catch (NumberFormatException e) {
                // Falls through to the message below
            }

            System.out.println("Please enter a price like 12995.50.");
        }
    }

    private boolean promptForYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String answer = scanner.nextLine().trim();

            if (answer.equalsIgnoreCase("y")) {
                return true;
            }

            if (answer.equalsIgnoreCase("n")) {
                return false;
            }

            System.out.println("Please enter y or n.");
        }
    }

    private String promptForEmail() {
        while (true) {
            String email = promptForText("Customer email: ");

            if (email.contains("@") && email.contains(".")) {
                return email;
            }

            System.out.println("Please enter a valid email, like name@example.com.");
        }
    }

    private int promptForNewVin() {
        while (true) {
            int vin = promptForWholeNumber("VIN: ");

            if (vin == 0) {
                System.out.println("The VIN must be greater than 0.");
            } else if (findVehicleByVin(vin) != null) {
                System.out.println("A vehicle with that VIN already exists.");
            } else {
                return vin;
            }
        }
    }

    private int promptForYear() {
        int latestYear = LocalDate.now().getYear() + 1;

        while (true) {
            int year = promptForWholeNumber("Year: ");

            if (year >= 1886 && year <= latestYear) {
                return year;
            }

            System.out.println("The year must be between 1886 and " + latestYear + ".");
        }
    }

    // Returns the type spelled the way the file spells it: car, truck, SUV, or van
    private String promptForVehicleType() {
        while (true) {
            String typed = promptForText("Type (car, truck, SUV, or van): ");

            for (String vehicleType : vehicleTypes) {
                if (vehicleType.equalsIgnoreCase(typed)) {
                    return vehicleType;
                }
            }

            System.out.println("The type must be car, truck, SUV, or van.");
        }
    }
}