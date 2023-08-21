package pl.kurs.javatestr3.validations.shapevalidations.factory;

import org.springframework.stereotype.Service;
import pl.kurs.javatestr3.exception.customexceptions.NoValidatorFoundForShapeTypeException;

import java.util.List;

@Service
public class ShapeValidatorFactory {

    private final List<ShapeValidator> shapeValidators;

    public ShapeValidatorFactory(List<ShapeValidator> shapeValidators) {
        this.shapeValidators = shapeValidators;
    }

    public ShapeValidator getValidator(String shapeType) {
        return shapeValidators.stream()
                .filter(validator -> validator.supports(shapeType))
                .findFirst()
                .orElseThrow(() -> new NoValidatorFoundForShapeTypeException("No validator found for shape type: " + shapeType));
    }
}
