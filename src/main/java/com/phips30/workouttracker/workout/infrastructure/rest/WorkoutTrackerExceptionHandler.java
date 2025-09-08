package com.phips30.workouttracker.workout.infrastructure.rest;

import com.phips30.workouttracker.workout.domain.usecase.RoutineNotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class WorkoutTrackerExceptionHandler extends ResponseEntityExceptionHandler {

    @Getter
    @Setter
    class ErrorBody {
        Date dateTime;
        String message;

        public ErrorBody(String message) {
            this.dateTime = new Date();
            this.message = message;
        }

        public Date getDateTime() {
            return dateTime;
        }

        public void setDateTime(Date dateTime) {
            this.dateTime = dateTime;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    @ExceptionHandler({
            Exception.class,
    })
    ResponseEntity<Object> handleGeneralExceptions(Exception ex, WebRequest request) {
        return super.handleExceptionInternal(ex, new ErrorBody(ex.getMessage()),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({
            RoutineNotFoundException.class,
    })
    ResponseEntity<Object> handleNotFoundExceptions(Exception ex, WebRequest request) {
        return super.handleExceptionInternal(ex, new ErrorBody(ex.getMessage()),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}