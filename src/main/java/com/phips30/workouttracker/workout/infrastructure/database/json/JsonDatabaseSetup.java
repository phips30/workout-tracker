package com.phips30.workouttracker.workout.infrastructure.database.json;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class JsonDatabaseSetup {

    private final Logger logger = LoggerFactory.getLogger(JsonDatabaseSetup.class);

    private final JsonDatabaseConfig jsonDatabaseConfig;

    public JsonDatabaseSetup(JsonDatabaseConfig jsonDatabaseConfig) {
        this.jsonDatabaseConfig = jsonDatabaseConfig;
    }

    @PostConstruct
    public void createJsonFile() throws IOException {
        createDbFiles(new File(jsonDatabaseConfig.getJson().getRoutineFilepath()));
        createDbFiles(new File(jsonDatabaseConfig.getJson().getExerciseFilepath()));
    }

    private void createDbFiles(File jsonDbFile) throws IOException {
        if (!jsonDbFile.exists()) {
            jsonDbFile.getParentFile().mkdirs();
            jsonDbFile.createNewFile();
            try (FileWriter writer = new FileWriter(jsonDbFile)) {
                writer.write("[]");
            }

            logger.info("Created json database file: {}", jsonDbFile.getAbsolutePath());
        }
    }
}
