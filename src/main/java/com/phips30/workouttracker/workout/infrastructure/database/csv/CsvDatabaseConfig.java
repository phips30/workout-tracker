package com.phips30.workouttracker.workout.infrastructure.database.csv;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "database")
@EnableConfigurationProperties
public class CsvDatabaseConfig {
    private Csv csv;

    public Csv getCsv() {
        return csv;
    }

    public void setCsv(Csv csv) {
        this.csv = csv;
    }

    public static class Csv {
        private String filepath;

        public String getFilepath() {
            return filepath;
        }

        public void setFilepath(String filepath) {
            this.filepath = filepath;
        }
    }
}
