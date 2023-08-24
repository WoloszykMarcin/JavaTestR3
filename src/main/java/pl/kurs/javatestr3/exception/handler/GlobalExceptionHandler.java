package pl.kurs.javatestr3.exception.handler;

import lombok.Builder;
import lombok.Value;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kurs.javatestr3.exception.constraints.ConstraintErrorHandler;
import pl.kurs.javatestr3.exception.customexceptions.*;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, ConstraintErrorHandler> constraintErrorMapper;

    public GlobalExceptionHandler(Set<ConstraintErrorHandler> handlers) {
        this.constraintErrorMapper = handlers.stream().collect(Collectors.toMap(ConstraintErrorHandler::getConstraintName, Function.identity()));
    }

    @ExceptionHandler(FigureNotFoundException.class)
    public ResponseEntity<String> handleFigureNotFoundException(FigureNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InvalidShapeTypeException.class)
    public ResponseEntity<String> handleInvalidShapeType(InvalidShapeTypeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidShapeParametersException.class)
    public ResponseEntity<String> handleInvalidParameters(InvalidShapeParametersException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<String> handleInvalidRoleException(InvalidRoleException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
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

    @ExceptionHandler(NoSuchFieldException.class)
    public ResponseEntity<String> handleNoSuchFieldException(NoSuchFieldException ex) {
        return new ResponseEntity<>("There is no such field as: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoValidatorFoundForShapeTypeException.class)
    public ResponseEntity<String> handleNoValidatorFoundForShapeTypeException(NoValidatorFoundForShapeTypeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WrongAttributeException.class)
    public ResponseEntity<String> handleWrongAttributeException(WrongAttributeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleConstraintViolationException(ConstraintViolationException exc) {
        String constraintName = exc.getConstraintName().substring(8, exc.getConstraintName().indexOf(" ") - 8);
        return ResponseEntity.badRequest().body(constraintErrorMapper.get(constraintName).mapToErrorDto());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException exc) {
        return ResponseEntity.badRequest().body(
                exc.getFieldErrors().stream().map(
                        fe -> new ValidationErrorDto(fe.getDefaultMessage(), fe.getField())
                ).collect(Collectors.toList())
        );
    }

    @Value
    public static class ValidationErrorDto {
        private String code;
        private String field;
    }

    @Value
    @Builder
    public static class UserAlreadyExistsErrorDto {
        private String email;
        private String code;
    }
}