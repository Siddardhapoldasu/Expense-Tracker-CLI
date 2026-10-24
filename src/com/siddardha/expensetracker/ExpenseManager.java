package com.siddardha.expensetracker;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/** Holds all expenses and contains the business logic. */
public class ExpenseManager {
    private final List<Expense> expenses = new ArrayList<>();
    private int nextId;

    public ExpenseManager(List<Expense> loaded) {
        expenses.addAll(loaded);
        nextId = expenses.stream().mapToInt(Expense::getId).max().orElse(0) + 1;
    }

    public Expense add(LocalDate date, String category, String description, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        Expense e = new Expense(nextId++, date, category, description, amount);
        expenses.add(e);
        return e;
    }

    public boolean delete(int id) {
        return expenses.removeIf(e -> e.getId() == id);
    }

    /** All expenses, newest first. */
    public List<Expense> getAll() {
        List<Expense> sorted = new ArrayList<>(expenses);
        sorted.sort(Comparator.comparing(Expense::getDate).reversed().thenComparing(Expense::getId));
        return Collections.unmodifiableList(sorted);
    }

    public List<Expense> byCategory(String category) {
        return getAll().stream()
                .filter(e -> e.getCategory().equalsIgnoreCase(category.trim()))
                .collect(Collectors.toList());
    }

    public double monthlyTotal(YearMonth month) {
        return expenses.stream()
                .filter(e -> YearMonth.from(e.getDate()).equals(month))
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    /** Total spent per category, sorted by category name. */
    public Map<String, Double> totalsByCategory() {
        return expenses.stream().collect(Collectors.groupingBy(
                Expense::getCategory, TreeMap::new, Collectors.summingDouble(Expense::getAmount)));
    }

    public List<Expense> raw() {
        return expenses;
    }
}
