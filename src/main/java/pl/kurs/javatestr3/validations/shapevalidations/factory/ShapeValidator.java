package pl.kurs.javatestr3.validations.shapevalidations.factory;

import pl.kurs.javatestr3.commands.CreateShapeCommand;

public interface ShapeValidator {
    boolean supports(String shapeType);
    void validate(CreateShapeCommand command);
}
