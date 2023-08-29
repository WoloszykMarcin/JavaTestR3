package pl.kurs.javatestr3.validations.shapevalidations.factory;

import pl.kurs.javatestr3.commands.ShapeCommand;

public interface ShapeValidator {
    boolean supports(String shapeType);

    void validate(ShapeCommand command);
}
