# Mos Eisley Motors: Car Dealership Application

 A Star Wars themed sales manger used vehicle dealership. It lets you search the inventory in several ways, add vehicles, and remove vehicles. Every change is saved to a CSV file, so the inventory is still there the next time you run it.


## Features

1. Find vehicles within a price range
2. Find vehicles by make and model
3. Find vehicles by year range
4. Find vehicles by color
5. Find vehicles by mileage range
6. Find vehicles by type (car, truck, SUV, van)
7. List all vehicles
8. Add a vehicle
9. Remove a vehicle (asks you to confirm first)
99. Quit

Searches ignore capital letters. Add and remove save the inventory file right away. Typing bad input (letters instead of numbers, blanks, an unknown vehicle type, a duplicate VIN) shows a helpful message instead of crashing.

## How to Run

**You need:** Java 17 (JDK) and IntelliJ IDEA.

1. Clone this repository: `git clone https://github.com/weseedonn/Bootcamp_Paul.git`
2. In IntelliJ, choose **File > Open** and select the **`CarDealership`** folder (the one that contains `pom.xml`).
3. Let Maven finish loading and check that the Project SDK is Java 17.
4. Open `src/main/java/org/example/Program.java` and click the green run arrow next to `main`.
5. Type a number from the menu and press Enter.

> The app finds `inventory.csv` using a path relative to the project folder, so open the `CarDealership` folder itself.

## Example Output

Menu option 6, searching for type `SUV`:

```
VIN      Year  Make        Model              Type   Color     Odometer        Price
13590    1977  Corellian   YT-1300 Freighter  SUV    Silver      999999   $59,995.00
14862    1983  Kuat        AT-ST Walker       SUV    Green        23400   $42,995.00
15099    2018  Kuat        Firespray-31       SUV    Green        72100   $48,995.00
```

## Data File

The inventory is stored in `src/main/resources/inventory.csv`, delimited by pipes. The first line is the dealership, and every line after it is one vehicle:

```
Mos Eisley Motors|Docking Bay 94, Mos Eisley, Tatooine|555-0077
10977|1977|SoroSuub|X-34 Landspeeder|car|Red|88420|4950.00
```

Vehicle lines are `vin|year|make|model|type|color|odometer|price`. A copy of the starting data is in `inventory_backup.csv`. Any unreadable line is skipped with a message when the file loads.

## Project Structure

| Class | What it does |
|---|---|
| `Program` | Starts the application |
| `UserInterface` | All screen output and typing, and calls the dealership to search, add, and remove |
| `Dealership` | Holds the dealership's details and its list of vehicles, with the search, add, and remove methods |
| `Vehicle` | One vehicle's information |
| `DealershipFileManager` | Reads and writes `inventory.csv` |

Prices use `BigDecimal` to avoid rounding errors with money. When saving, the app writes to a temporary file and then swaps it in, so a failure partway through cannot wipe out the inventory.

## Author

Your Name, Java Bootcamp, Fall 2026