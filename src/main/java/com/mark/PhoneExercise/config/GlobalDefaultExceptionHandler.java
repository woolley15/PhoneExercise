package com.mark.PhoneExercise.config;

import com.mark.PhoneExercise.model.ErrorMessage;
import org.hibernate.PropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    @ExceptionHandler(PropertyValueException.class)
    public ResponseEntity<ErrorMessage> propertyValueException(PropertyValueException ex, WebRequest request) {
        ErrorMessage message =  new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                ex.getPropertyName() + ex.getMessage() + ex.getEntityName(),
                request.getDescription(false));

        return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
    }

}
