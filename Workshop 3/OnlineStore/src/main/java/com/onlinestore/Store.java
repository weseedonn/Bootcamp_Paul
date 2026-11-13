package com.onlinestore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Store {

    private final Scanner scanner = new Scanner(System.in);
    private final List<Product> inventory;
    private final Cart cart = new Cart();

    public Store(String productFilePath) {
        this.inventory = ProductLoader.loadProducts(productFilePath);
    }

    // ------------------------------------------------------------------
    // Home screen
    // ------------------------------------------------------------------

    public void run() {
        if (inventory.isEmpty()) {
            System.out.println("No products were loaded. Check that products.csv is in the project folder.");
            return;
        }

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("=========== WELCOME TO THE ONLINE STORE ===========");
            System.out.println("  1) Display Products");
            System.out.println("  2) Display Cart");
            System.out.println("  0) Exit");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> displayProductsScreen();
                case "2" -> displayCartScreen();
                case "0" -> {
                    System.out.println("Thanks for visiting. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice, please enter 1, 2 or 0.");
            }
        }
    }

    // ------------------------------------------------------------------
    // Display Products screen
    // ------------------------------------------------------------------

    private void displayProductsScreen() {
        printProducts(inventory);

        boolean viewing = true;
        while (viewing) {
            System.out.println();
            System.out.println("------------------- PRODUCTS --------------------");
            System.out.println("  1) Search by Product Name");
            System.out.println("  2) Search by Price Range");
            System.out.println("  3) Search by Department");
            System.out.println("  4) Show All Products");
            System.out.println("  5) Add a Product to Cart");
            System.out.println("  0) Go Back to Home Screen");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> searchByName();
                case "2" -> searchByPriceRange();
                case "3" -> searchByDepartment();
                case "4" -> printProducts(inventory);
                case "5" -> addProductToCart();
                case "0" -> viewing = false;
                default -> System.out.println("Invalid choice, please enter a number from the menu.");
            }
        }
    }

    private void searchByName() {
        System.out.print("Enter part of the product name: ");
        String searchText = scanner.nextLine().trim().toLowerCase();

        List<Product> matches = new ArrayList<>();
        for (Product product : inventory) {
            if (product.getProductName().toLowerCase().contains(searchText)) {
                matches.add(product);
            }
        }
        printProducts(matches);
    }

    private void searchByPriceRange() {
        double minPrice = readDouble("Enter the minimum price: ");
        double maxPrice = readDouble("Enter the maximum price: ");

        if (minPrice > maxPrice) {
            System.out.println("The minimum price cannot be higher than the maximum price.");
            return;
        }

        List<Product> matches = new ArrayList<>();
        for (Product product : inventory) {
            if (product.getPrice() >= minPrice && product.getPrice() <= maxPrice) {
                matches.add(product);
            }
        }
        printProducts(matches);
    }

    private void searchByDepartment() {
        System.out.print("Enter the department (example: Games): ");
        String searchText = scanner.nextLine().trim().toLowerCase();

        List<Product> matches = new ArrayList<>();
        for (Product product : inventory) {
            if (product.getDepartment().toLowerCase().contains(searchText)) {
                matches.add(product);
            }
        }
        printProducts(matches);
    }

    private void addProductToCart() {
        System.out.print("Enter the SKU of the product to add: ");
        String sku = scanner.nextLine().trim();

        Product product = findProductBySku(sku);
        if (product == null) {
            System.out.println("No product found with SKU \"" + sku + "\".");
            return;
        }

        cart.addProduct(product);
        System.out.println("Added to cart: " + product.getProductName());
    }

    private Product findProductBySku(String sku) {
        for (Product product : inventory) {
            if (product.getSku().equalsIgnoreCase(sku)) {
                return product;
            }
        }
        return null;
    }

    private void printProducts(List<Product> products) {
        System.out.println();
        if (products.isEmpty()) {
            System.out.println("No products matched your search.");
            return;
        }

        System.out.printf("%-8s %-35s %9s  %s%n", "SKU", "Product Name", "Price", "Department");
        System.out.println("-----------------------------------------------------------------------");
        for (Product product : products) {
            System.out.println(product);
        }
    }

    // ------------------------------------------------------------------
    // Display Cart screen
    // ------------------------------------------------------------------

    private void displayCartScreen() {
        boolean viewing = true;
        while (viewing) {
            printCart();

            System.out.println();
            System.out.println("-------------------- CART --------------------");
            System.out.println("  1) Check Out");
            System.out.println("  2) Remove Product from Cart");
            System.out.println("  0) Go Back to Home Screen");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> {
                    checkOut();
                    // After a successful checkout the cart is empty, so return home
                    if (cart.isEmpty()) {
                        viewing = false;
                    }
                }
                case "2" -> removeProductFromCart();
                case "0" -> viewing = false;
                default -> System.out.println("Invalid choice, please enter 1, 2 or 0.");
            }
        }
    }

    private void printCart() {
        System.out.println();
        System.out.println("==================== YOUR CART ====================");
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        for (CartItem item : cart.getItems()) {
            System.out.println(item);
        }
        System.out.println("---------------------------------------------------");
        System.out.printf("Cart Total: $%.2f%n", cart.getTotal());
    }

    private void removeProductFromCart() {
        if (cart.isEmpty()) {
            System.out.println("There is nothing in your cart to remove.");
            return;
        }

        System.out.print("Enter the SKU of the product to remove: ");
        String sku = scanner.nextLine().trim();

        if (cart.removeProduct(sku)) {
            System.out.println("Removed " + sku + " from your cart.");
        } else {
            System.out.println("That SKU is not in your cart.");
        }
    }

    // ------------------------------------------------------------------
    // Check Out (bonus)
    // ------------------------------------------------------------------

    private void checkOut() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty, so there is nothing to check out.");
            return;
        }

        double total = cart.getTotal();
        System.out.println();
        System.out.printf("Total amount owed: $%.2f%n", total);

        double amountPaid = 0;
        boolean paymentAccepted = false;
        while (!paymentAccepted) {
            amountPaid = readDouble("Enter your cash payment (or 0 to cancel): $");

            if (amountPaid == 0) {
                System.out.println("Checkout canceled. Your cart was not changed.");
                return;
            }
            // Compare in cents to avoid decimal rounding problems
            if (Math.round(amountPaid * 100) < Math.round(total * 100)) {
                System.out.println("That is not enough to cover the total. Please try again.");
            } else {
                paymentAccepted = true;
            }
        }

        double change = Math.round((amountPaid - total) * 100) / 100.0;
        LocalDateTime orderDate = LocalDateTime.now();

        String receipt = ReceiptWriter.buildReceipt(cart, amountPaid, change, orderDate);
        System.out.println();
        System.out.println(receipt);

        String savedPath = ReceiptWriter.saveReceipt(receipt, orderDate);
        if (savedPath != null) {
            System.out.println("Receipt saved to " + savedPath);
        }

        cart.clear();
        System.out.println("Thank you for your purchase! Returning to the home screen.");
    }

    // ------------------------------------------------------------------
    // Input helper
    // ------------------------------------------------------------------

    /** Keeps asking until the user types a valid, non-negative number. */
    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value < 0) {
                    System.out.println("Please enter a number that is 0 or greater.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("That is not a valid number. Try again.");
            }
        }
    }
}
