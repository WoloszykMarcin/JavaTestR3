package pl.kurs.javatestr3.exception.customexceptions;

public class NoValidatorFoundForShapeTypeException extends RuntimeException {
    public NoValidatorFoundForShapeTypeException(String message) {
        super(message);
    }
}
