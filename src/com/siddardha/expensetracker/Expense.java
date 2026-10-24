package com.siddardha.expensetracker;

import java.time.LocalDate;

/** Represents a single expense entry. */
public class Expense {
    private final int id;
    private final LocalDate date;
    private final String category;
    private final String description;
    private final double amount;

    public Expense(int id, LocalDate date, String category, String description, double amount) {
        this.id = id;
        this.date = date;
        this.category = formatCategory(category);
        this.description = description.trim().replaceAll(",\\s*", " ");
        this.amount = amount;
    }

    /** Makes categories consistent, e.g. "FOOD " becomes "Food". */
    private static String formatCategory(String raw) {
        String c = raw.trim().replaceAll(",\\s*", " ");
        if (c.isEmpty()) {
            return "General";
        }
        return c.substring(0, 1).toUpperCase() + c.substring(1).toLowerCase();
    }

    public int getId() { return id; }
    public LocalDate getDate() { return date; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }

    /** One line of the CSV data file. */
    public String toCsv() {
        return id + "," + date + "," + category + "," + description + "," + amount;
    }

    /** Builds an Expense from one CSV line. Throws if the line is invalid. */
    public static Expense fromCsv(String line) {
        String[] p = line.split(",", 5);
        if (p.length < 5) {
            throw new IllegalArgumentException("Invalid line: " + line);
        }
        return new Expense(Integer.parseInt(p[0]), LocalDate.parse(p[1]), p[2], p[3], Double.parseDouble(p[4]));
    }

    @Override
    public String toString() {
        return String.format("%-4d %-12s %-14s %-24s %10.2f", id, date, category, description, amount);
    }
}
