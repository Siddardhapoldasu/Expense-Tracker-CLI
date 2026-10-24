# Expense Tracker (Java CLI)

A simple console application to record and review daily expenses. Data is saved to a CSV file, so it is still there the next time you run the program.

## Features
- Add an expense with date, category, description and amount
- View all expenses (newest first) with a running total
- Filter expenses by category
- See the total spent in any month
- See a summary of spending per category
- Delete an expense by ID
- Input validation for dates, amounts and menu choices
- Automatic saving to `data/expenses.csv`

## Concepts Used
- Object-oriented design (separate classes for model, logic, storage and UI)
- Collections (`List`, `Map`) and Java Streams (`filter`, `mapToDouble`, `groupingBy`)
- File I/O with `java.nio.file`
- Date handling with `java.time` (`LocalDate`, `YearMonth`)
- Exception handling for invalid input and file errors

## Project Structure
```
Expense-Tracker-CLI/
├── src/com/siddardha/expensetracker/
│   ├── Main.java            # Console menu and user input
│   ├── Expense.java         # Expense model (one entry)
│   ├── ExpenseManager.java  # Add, delete, filter and total logic
│   └── FileStorage.java     # Save and load the CSV file
├── .gitignore
└── README.md
```

## How to Run
Requires JDK 17 or newer.

```bash
javac -d out src/com/siddardha/expensetracker/*.java
java -cp out com.siddardha.expensetracker.Main
```

## Sample Output
```
ID   Date         Category       Description                  Amount
3    2026-10-24   Food           Snacks                        80.00
2    2026-10-22   Travel         Bus pass                     600.00
1    2026-10-20   Food           Lunch with friends           250.50
Total: 930.50
```

## Possible Improvements
- Edit an existing expense
- Monthly budget limits with warnings
- Export reports, or a GUI/web front end

## Author
Poldasu Govardhan Siddardha
