package pl.kurs.javatestr3.validations.shapevalidations;

import org.springframework.stereotype.Service;
import pl.kurs.javatestr3.commands.CreateShapeCommand;
import pl.kurs.javatestr3.exception.customexceptions.InvalidShapeTypeException;
import pl.kurs.javatestr3.validations.shapevalidations.factory.ShapeValidator;
import pl.kurs.javatestr3.validations.shapevalidations.factory.ShapeValidatorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

@Service
public class ShapeCommandValidator implements ConstraintValidator<ValidShapeCommand, CreateShapeCommand> {

    private ShapeValidatorFactory shapeValidatorFactory;

    public ShapeCommandValidator(ShapeValidatorFactory shapeValidatorFactory) {
        this.shapeValidatorFactory = shapeValidatorFactory;
    }

    @Override
    public void initialize(ValidShapeCommand constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(CreateShapeCommand command, ConstraintValidatorContext context) {
        if (!isValidShapeType(command.getShapeType()))
            throw new InvalidShapeTypeException("Invalid shape type: " + command.getShapeType());

        ShapeValidator validator = shapeValidatorFactory.getValidator(command.getShapeType());
        validator.validate(command);

        return true;
    }

    private boolean isValidShapeType(String shapeType) {
        return Arrays.asList("circle", "square", "rectangle", "triangle").contains(shapeType);
    }
}
