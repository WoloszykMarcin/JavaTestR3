package pl.kurs.javatestr3.exception.customexceptions;

public class InvalidShapeParametersException extends RuntimeException {
    public InvalidShapeParametersException(String message) {
        super(message);
    }
}
