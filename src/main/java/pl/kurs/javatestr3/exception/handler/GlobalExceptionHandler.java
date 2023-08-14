package pl.kurs.javatestr3.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kurs.javatestr3.exception.InvalidDateFormatException;
import pl.kurs.javatestr3.exception.InvalidShapeParametersException;
import pl.kurs.javatestr3.exception.InvalidShapeTypeException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidShapeTypeException.class)
    public ResponseEntity<String> handleInvalidShapeType(InvalidShapeTypeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidShapeParametersException.class)
    public ResponseEntity<String> handleInvalidParameters(InvalidShapeParametersException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<Object> handleClassCastException(ClassCastException ex) {
        String errorMessage = "Invalid parameter type, expected Double";
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDateFormatException.class)
    public ResponseEntity<String> handleInvalidDateFormat(InvalidDateFormatException ex) {
        return new ResponseEntity<>("Invalid Date Format! Sample format: 2023-08-13T11:49:40.857432", HttpStatus.BAD_REQUEST);
    }
}