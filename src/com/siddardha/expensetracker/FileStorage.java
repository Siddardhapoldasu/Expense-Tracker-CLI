package com.siddardha.expensetracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Saves and loads expenses from a CSV file so data is kept between runs. */
public class FileStorage {
    private final Path file;

    public FileStorage(String path) {
        this.file = Path.of(path);
    }

    public List<Expense> load() {
        List<Expense> list = new ArrayList<>();
        if (!Files.exists(file)) {
            return list;
        }
        try {
            for (String line : Files.readAllLines(file)) {
                if (line.isBlank()) {
                    continue;
                }
                try {
                    list.add(Expense.fromCsv(line));
                } catch (RuntimeException ex) {
                    System.out.println("Skipped a bad line in data file: " + line);
                }
            }
        } catch (IOException ex) {
            System.out.println("Could not read data file: " + ex.getMessage());
        }
        return list;
    }

    public void save(List<Expense> expenses) {
        try {
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
            }
            List<String> lines = new ArrayList<>();
            for (Expense e : expenses) {
                lines.add(e.toCsv());
            }
            Files.write(file, lines);
        } catch (IOException ex) {
            System.out.println("Could not save data: " + ex.getMessage());
        }
    }
}
