package com.phips30.workouttracker.workout.infrastructure.database.json;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "database")
@EnableConfigurationProperties
public class JsonDatabaseConfig {
    private Json json;

    public Json getJson() {
        return json;
    }

    public void setJson(Json json) {
        this.json = json;
    }

    public static class Json {
        private String routineFilepath;
        private String exerciseFilepath;

        public String getRoutineFilepath() {
            return routineFilepath;
        }

        public void setRoutineFilepath(String routineFilepath) {
            this.routineFilepath = routineFilepath;
        }

        public String getExerciseFilepath() {
            return exerciseFilepath;
        }

        public void setExerciseFilepath(String exerciseFilepath) {
            this.exerciseFilepath = exerciseFilepath;
        }
    }
}
