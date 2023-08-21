package pl.kurs.javatestr3.exception.customexceptions;

public class FigureNotFoundException extends RuntimeException {
    public FigureNotFoundException(String message) {
        super(message);
    }
}
