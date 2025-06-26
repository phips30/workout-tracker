package com.phips30.workouttracker.workout.infrastructure.database.csv;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class CsvDatabaseSetup {

    private final CsvDatabaseConfig csvDatabaseConfig;

    public CsvDatabaseSetup(CsvDatabaseConfig csvDatabaseConfig) {
        this.csvDatabaseConfig = csvDatabaseConfig;
    }

    @PostConstruct
    public void createCsvFile() throws IOException {
        File csvDbFile = new File(csvDatabaseConfig.getCsv().getFilepath());
        if (!csvDbFile.exists()) {
            csvDbFile.getParentFile().mkdirs();
            csvDbFile.createNewFile();

            System.out.println("Created csv database file: " + csvDatabaseConfig.getCsv().getFilepath());

        }
    }
}
