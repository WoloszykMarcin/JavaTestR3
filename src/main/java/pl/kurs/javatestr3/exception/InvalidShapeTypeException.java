package pl.kurs.javatestr3.exception;

public class InvalidShapeTypeException extends RuntimeException {
    public InvalidShapeTypeException(String shapeType) {
        super("Invalid shape type: " + shapeType);
    }
}
