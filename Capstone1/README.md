# Accounting Ledger Application

A command-line (CLI) Java application for tracking financial transactions for a business or for personal use. Every transaction is saved to and read from a CSV file, so your data is still there the next time you run the app.

Built with Java 17 and Maven as my mini capstone project for the Java bootcamp.

## Features

- **Home screen:** add a deposit, make a payment, open the ledger, or exit. The app keeps running until you choose to exit.
- **Ledger:** view All entries, only Deposits, or only Payments. Newest entries are always shown first.
- **Reports:** Month To Date, Previous Month, Year To Date, Previous Year, and Search by Vendor.
- **Custom Search (bonus feature):** combine any of these filters: start date, end date, description, vendor, and exact amount. Leave a field blank to skip it.
- **Input validation:** blank entries, letters in amount fields, impossible dates, and the `|` character are all rejected with a helpful message, so the app does not crash on bad input.

## How to Run

**You need:** Java 17 (JDK) and IntelliJ IDEA.

1. Clone this repository: `git clone https://github.com/weseedonn/Bootcamp_Paul.git`
2. In IntelliJ, choose **File > Open** and select the **`Capstone1`** folder (the one that contains `pom.xml`), not the whole repository.
3. Let Maven finish loading. Check that the Project SDK is Java 17 (File > Project Structure).
4. Open `src/main/java/org/example/Main.java` and click the green run arrow next to `main`.
5. Use the menus in the Run window: type the letter or number shown, then press Enter.

> The app finds `transactions.csv` using a path relative to the project folder, so open the `Capstone1` folder itself.

## Using the App

```
=== ACCOUNTING LEDGER ===
D) Add Deposit
P) Make Payment (Debit)
L) Ledger
X) Exit
```

From **L) Ledger** you can choose A) All, D) Deposits, P) Payments, R) Reports, or H) Home.
From **R) Reports** you can run options 1 to 6, or 0 to go back.

Example ledger output:

```
--- ALL TRANSACTIONS ---
Date        Time      Description                   Vendor                  Amount
2026-11-03  09:48:20  Groceries                     Whole Foods             -96.42
2026-11-02  13:15:36  Invoice 1030 paid             Joe                    2000.00
2026-11-01  08:30:14  Monthly rent                  Landlord Co           -1200.00
```

## Data File

Transactions are stored in `src/main/resources/transactions.csv`, one per line, in this format:

```
date|time|description|vendor|amount
2026-11-01|08:30:14|Monthly rent|Landlord Co|-1200.00
```

- Deposits are saved as positive amounts and payments as negative amounts.
- The date and time are filled in automatically from the computer's clock.
- Any unreadable line is skipped with a message instead of crashing the app.

## Project Structure

| File | What it does |
|---|---|
| `Main.java` | Home screen and adding deposits and payments |
| `LedgerScreen.java` | Ledger screen: All, Deposits, Payments (newest first) |
| `ReportsScreen.java` | Reports screen: date reports, vendor search, custom search |
| `Transaction.java` | One transaction, plus converting to and from a CSV line |
| `TransactionFileManager.java` | Reading and writing `transactions.csv` |
| `InputHelper.java` | Prompts that keep asking until the input is valid |

## How It Works

- **Newest first:** transactions are sorted by date and then time, and the order is reversed.
- **Date reports:** all four share one method that keeps the transactions between a start date and an end date, both included. "Previous Month" uses the first day of this month minus one day, so it works for 28, 29, 30, and 31 day months and across New Year.
- **Safe file writing:** when saving, the app adds a line break first if the file does not already end with one. This fixed a bug I found while testing, where a new entry was glued to the end of the last line.

## Project Progress

- [x] Transaction model and CSV reading and writing
- [x] Home screen with deposits and payments
- [x] Input validation
- [x] Ledger screens (All, Deposits, Payments)
- [x] Reports and vendor search
- [x] Custom search (bonus)
- [x] README
- [x] Final code cleanup (formatting and inspection)


## Author

Adonnis Paul, Java Bootcamp, Fall 2026