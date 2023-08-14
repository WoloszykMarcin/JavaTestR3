package pl.kurs.javatestr3.validations;

import org.springframework.stereotype.Service;
import pl.kurs.javatestr3.commands.CreateShapeCommand;
import pl.kurs.javatestr3.exception.InvalidShapeParametersException;
import pl.kurs.javatestr3.exception.InvalidShapeTypeException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

@Service
public class ShapeCommandValidator implements ConstraintValidator<ValidShapeCommand, CreateShapeCommand> {

    @Override
    public void initialize(ValidShapeCommand constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(CreateShapeCommand command, ConstraintValidatorContext context) {
        boolean valid = true;

        if (!isValidShapeType(command.getShapeType())) {
            throw new InvalidShapeTypeException("Invalid shape type: " + command.getShapeType());
        }

        switch (command.getShapeType()) {
            case "circle":
                if (!command.getParameters().containsKey("radius")) {
                    throw new InvalidShapeParametersException("Circle requires a 'radius' parameter.");
                }
                break;
            case "rectangle":
                if (!command.getParameters().containsKey("length") || !command.getParameters().containsKey("width")) {
                    throw new InvalidShapeParametersException("Rectangle requires both 'length' and 'width' parameters.");
                }
                break;
            case "square":
                if (!command.getParameters().containsKey("side")) {
                    throw new InvalidShapeParametersException("Square requires a 'side' parameter.");
                }
                break;
        }

        return valid;
    }

    private boolean isValidShapeType(String shapeType) {
        return Arrays.asList("circle", "square", "rectangle").contains(shapeType);
    }
}
