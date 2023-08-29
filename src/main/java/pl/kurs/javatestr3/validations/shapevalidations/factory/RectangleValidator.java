package pl.kurs.javatestr3.validations.shapevalidations.factory;

import org.springframework.stereotype.Service;
import pl.kurs.javatestr3.commands.ShapeCommand;
import pl.kurs.javatestr3.exception.customexceptions.InvalidShapeParametersException;

@Service
public class RectangleValidator implements ShapeValidator {

    @Override
    public boolean supports(String shapeType) {
        return "rectangle".equals(shapeType);
    }

    @Override
    public void validate(ShapeCommand command) {
        if (!command.getParameters().containsKey("length") || !command.getParameters().containsKey("width"))
            throw new InvalidShapeParametersException("Rectangle requires both 'length' and 'width' parameters.");

        double length = (double) command.getParameters().get("length");
        double width = (double) command.getParameters().get("width");

        if (length <= 0)
            throw new InvalidShapeParametersException("Rectangle length must be greater than 0.");
        if (width <= 0)
            throw new InvalidShapeParametersException("Rectangle width must be greater than 0.");
    }
}
