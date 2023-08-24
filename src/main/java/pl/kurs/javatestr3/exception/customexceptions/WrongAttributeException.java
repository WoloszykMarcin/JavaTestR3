package pl.kurs.javatestr3.exception.customexceptions;

public class WrongAttributeException extends RuntimeException {
    public WrongAttributeException(String message) {
        super(message);
    }
}
