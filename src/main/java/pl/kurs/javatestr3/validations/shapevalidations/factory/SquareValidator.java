package pl.kurs.javatestr3.validations.shapevalidations.factory;

import org.springframework.stereotype.Service;
import pl.kurs.javatestr3.commands.ShapeCommand;
import pl.kurs.javatestr3.exception.customexceptions.InvalidShapeParametersException;

@Service
public class SquareValidator implements ShapeValidator {

    @Override
    public boolean supports(String shapeType) {
        return "square".equals(shapeType);
    }

    @Override
    public void validate(ShapeCommand command) {
        if (!command.getParameters().containsKey("side"))
            throw new InvalidShapeParametersException("Square requires a 'side' parameter.");

        double side = (double) command.getParameters().get("side");

        if (side <= 0)
            throw new InvalidShapeParametersException("Square side length must be greater than 0.");
    }
}
