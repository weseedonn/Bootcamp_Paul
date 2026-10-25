package org.example;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);

    Book[] books = {
            new Book(1, "1001", "Harry Potter"),
            new Book(2, "1002", "The Hobbit"),
            new Book(3, "1003", "Percy Jackson"),
            new Book(4, "1004", "Dark Lord: The rise of Darth Vader"),
            new Book(5, "1005", "Dune"),
            new Book(6, "1006", "The Hunger Games"),
            new Book(7, "1007", "Twilight"),
            new Book(8, "1008", "The Great Gatsby"),
            new Book(9, "1009", "Wonder"),
            new Book(10, "1010", "Holes"),
            new Book(11, "1011", "Hatchet"),
            new Book(12, "1012", "The Giver"),
            new Book(13, "1013", "Divergent"),
            new Book(14, "1014", "Ender's Game"),
            new Book(15, "1015", "Charlotte's Web"),
            new Book(16, "1016", "Fahrenheit 451"),
            new Book(17, "1017", "The Maze Runner"),
            new Book(18, "1018", "Lord of the Flies"),
            new Book(19, "1019", "To Kill a Mockingbird"),
            new Book(20, "1020", "Animal Farm")

    };

        while (true) {

            System.out.println("\n=== NEIGHBORHOOD LIBRARY ===");
            System.out.println("1 - Show Available Books");
            System.out.println("2 - Show Checked Out Books");
            System.out.println("3 - Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                showAvailableBooks(books, scanner);
            }
            else if (choice == 2) {
                showCheckedOutBooks(books, scanner);
            }
            else if (choice == 3) {
                System.out.println("Goodbye!");
                System.exit(0);
            }
            else {
                System.out.println("Invalid choice.");
            }


    }
}

    public static void showAvailableBooks(Book[] books, Scanner scanner) {

        System.out.println("\nAVAILABLE BOOKS");

        for (Book book : books) {
            if (!book.getIsCheckedOut()) {
                System.out.println(book.getId() + " - " + book.getTitle());
            }
        }

        System.out.print("\nEnter book id to check out (0 to return): ");
        int bookId = scanner.nextInt();
        scanner.nextLine();

        if (bookId == 0) {
            return;
        }

        for (Book book : books) {

            if (book.getId() == bookId && !book.getIsCheckedOut()) {

                System.out.print("Enter your name: ");
                String name = scanner.nextLine();

                book.checkOut(name);

                System.out.println("Book checked out successfully!");
                return;
            }
        }

        System.out.println("Book not found.");
    }

    public static void showCheckedOutBooks(Book[] books, Scanner scanner) {

        boolean foundBook = false;

        System.out.println("\nCHECKED OUT BOOKS");

        for (Book book : books) {

            if (book.getIsCheckedOut()) {

                foundBook = true;

                System.out.println(
                        book.getId()
                                + " - "
                                + book.getTitle()
                                + " (Checked out to "
                                + book.getCheckedOutTo()
                                + ")"
                );
            }
        }

        if (!foundBook) {
            System.out.println("No books are currently checked out.");
            return;
        }

        System.out.print("\nC to Check In a Book or X to Return Home: ");
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("C")) {

            System.out.print("Enter Book ID: ");
            int bookId = scanner.nextInt();
            scanner.nextLine();

            for (Book book : books) {

                if (book.getId() == bookId && book.getIsCheckedOut()) {

                    book.checkIn();

                    System.out.println("Book checked in successfully!");
                    return;
                }
            }

            System.out.println("Book not found.");
        }
    }
}
