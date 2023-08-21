package pl.kurs.javatestr3.validations.shapevalidations.factory;

import org.springframework.stereotype.Service;
import pl.kurs.javatestr3.commands.CreateShapeCommand;
import pl.kurs.javatestr3.exception.customexceptions.InvalidShapeParametersException;

@Service
public class RectangleValidator implements ShapeValidator {

    @Override
    public boolean supports(String shapeType) {
        return "rectangle".equals(shapeType);
    }

    @Override
    public void validate(CreateShapeCommand command) {
        if (!command.getParameters().containsKey("length") || !command.getParameters().containsKey("width")) {
            throw new InvalidShapeParametersException("Rectangle requires both 'length' and 'width' parameters.");
        }
    }
}
