<<<<<<< HEAD
# Online Store

A command line Online Store application written in Java 17 (Maven project).
Customers can browse products, search and filter them, manage a shopping cart,
check out with cash, and get a printed and saved sales receipt.

## Features

- Loads the store inventory from `products.csv` (pipe-delimited)
- **Home screen:** Display Products, Display Cart, Exit
- **Display Products:** view all products, search by product name, price range, or department, add a product to the cart by SKU
- **Display Cart:** view line items with quantities and the cart total, remove a product, check out
- **Check Out:** enter a cash payment, the app verifies it covers the total, calculates change, prints a receipt, and clears the cart
- Duplicate items show once in the cart with a quantity
- Receipts are saved to the `Receipts/` folder as `yyyyMMdd-HHmmss.txt`

## Project Structure

```
online-store
├── pom.xml
├── products.csv
└── src/main/java/com/onlinestore
    ├── Main.java            (starts the app)
    ├── Store.java           (all screens and menus)
    ├── Product.java         (product data)
    ├── CartItem.java        (a product + quantity)
    ├── Cart.java            (add, remove, total)
    ├── ProductLoader.java   (reads products.csv)
    └── ReceiptWriter.java   (builds and saves receipts)
```

## How to Run

1. Open the project in IntelliJ (Java 17, Maven).
2. Make sure `products.csv` is in the project root folder.
3. Run `Main.java`.

## Example Session

```
=========== WELCOME TO THE ONLINE STORE ===========
  1) Display Products
  2) Display Cart
  0) Exit
```

## Team

Add your team members' names here.
=======
# Bootcamp_Paul
My classroom Github repol
>>>>>>> 0372157eb8ea548f8de2d67013af8d3966a9ee90
