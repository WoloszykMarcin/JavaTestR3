package pl.kurs.javatestr3.exception.constraints;

import pl.kurs.javatestr3.exception.handler.GlobalExceptionHandler;

public interface ConstraintErrorHandler {
    GlobalExceptionHandler.ValidationErrorDto mapToErrorDto();

    String getConstraintName();
}