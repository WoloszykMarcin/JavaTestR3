package pl.kurs.javatestr3.validations.shapevalidations.factory;

import org.springframework.stereotype.Service;
import pl.kurs.javatestr3.commands.ShapeCommand;
import pl.kurs.javatestr3.exception.customexceptions.InvalidShapeParametersException;

@Service
public class CircleValidator implements ShapeValidator {

    @Override
    public boolean supports(String shapeType) {
        return "circle".equals(shapeType);
    }

    @Override
    public void validate(ShapeCommand command) {
        if (!command.getParameters().containsKey("radius"))
            throw new InvalidShapeParametersException("Circle requires a 'radius' parameter.");

        double radius = (double) command.getParameters().get("radius");

        if (radius <= 0)
            throw new InvalidShapeParametersException("Circle radius must be greater than 0.");
    }
}
