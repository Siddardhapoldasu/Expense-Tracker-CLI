package com.siddardha.expensetracker;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Console menu for the Expense Tracker. */
public class Main {
    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        FileStorage storage = new FileStorage("data/expenses.csv");
        ExpenseManager manager = new ExpenseManager(storage.load());

        System.out.println("=== Expense Tracker ===");
        try {
            boolean running = true;
            while (running) {
                printMenu();
                String choice = ask("Choose an option: ");
                switch (choice) {
                    case "1" -> addExpense(manager, storage);
                    case "2" -> printList(manager.getAll());
                    case "3" -> printList(manager.byCategory(ask("Category: ")));
                    case "4" -> showMonthlyTotal(manager);
                    case "5" -> showCategorySummary(manager);
                    case "6" -> deleteExpense(manager, storage);
                    case "7" -> running = false;
                    default -> System.out.println("Invalid option. Please enter 1-7.");
                }
            }
        } catch (NoSuchElementException ex) {
            // Input ended (for example Ctrl+D); fall through and save.
        }
        storage.save(manager.raw());
        System.out.println("Data saved. Goodbye!");
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1. Add expense");
        System.out.println("2. View all expenses");
        System.out.println("3. View expenses by category");
        System.out.println("4. Monthly total");
        System.out.println("5. Category summary");
        System.out.println("6. Delete expense");
        System.out.println("7. Exit");
    }

    private static String ask(String prompt) {
        System.out.print(prompt);
        return in.nextLine().trim();
    }

    private static void addExpense(ExpenseManager manager, FileStorage storage) {
        try {
            String dateText = ask("Date (yyyy-mm-dd, blank for today): ");
            LocalDate date = dateText.isEmpty() ? LocalDate.now() : LocalDate.parse(dateText);
            String category = ask("Category (e.g. Food, Travel): ");
            String description = ask("Description: ");
            double amount = Double.parseDouble(ask("Amount: "));
            Expense e = manager.add(date, category, description, amount);
            storage.save(manager.raw());
            System.out.println("Added expense with ID " + e.getId());
        } catch (DateTimeParseException ex) {
            System.out.println("Invalid date. Use the format yyyy-mm-dd.");
        } catch (NumberFormatException ex) {
            System.out.println("Invalid amount. Please enter a number.");
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void printList(List<Expense> list) {
        if (list.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }
        System.out.printf("%-4s %-12s %-14s %-24s %10s%n", "ID", "Date", "Category", "Description", "Amount");
        list.forEach(System.out::println);
        double total = list.stream().mapToDouble(Expense::getAmount).sum();
        System.out.printf("Total: %.2f%n", total);
    }

    private static void showMonthlyTotal(ExpenseManager manager) {
        try {
            String text = ask("Month (yyyy-mm, blank for this month): ");
            YearMonth month = text.isEmpty() ? YearMonth.now() : YearMonth.parse(text);
            System.out.printf("Total for %s: %.2f%n", month, manager.monthlyTotal(month));
        } catch (DateTimeParseException ex) {
            System.out.println("Invalid month. Use the format yyyy-mm.");
        }
    }

    private static void showCategorySummary(ExpenseManager manager) {
        Map<String, Double> totals = manager.totalsByCategory();
        if (totals.isEmpty()) {
            System.out.println("No expenses yet.");
            return;
        }
        totals.forEach((cat, sum) -> System.out.printf("%-14s %10.2f%n", cat, sum));
    }

    private static void deleteExpense(ExpenseManager manager, FileStorage storage) {
        try {
            int id = Integer.parseInt(ask("Expense ID to delete: "));
            if (manager.delete(id)) {
                storage.save(manager.raw());
                System.out.println("Deleted.");
            } else {
                System.out.println("No expense with that ID.");
            }
        } catch (NumberFormatException ex) {
            System.out.println("Invalid ID. Please enter a number.");
        }
    }
}
