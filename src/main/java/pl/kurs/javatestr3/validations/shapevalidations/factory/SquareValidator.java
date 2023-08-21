package pl.kurs.javatestr3.validations.shapevalidations.factory;

import org.springframework.stereotype.Service;
import pl.kurs.javatestr3.commands.CreateShapeCommand;
import pl.kurs.javatestr3.exception.customexceptions.InvalidShapeParametersException;

@Service
public class SquareValidator implements ShapeValidator {

    @Override
    public boolean supports(String shapeType) {
        return "square".equals(shapeType);
    }

    @Override
    public void validate(CreateShapeCommand command) {
        if (!command.getParameters().containsKey("side")) {
            throw new InvalidShapeParametersException("Square requires a 'side' parameter.");
        }
    }
}
