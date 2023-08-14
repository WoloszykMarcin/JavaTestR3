package pl.kurs.javatestr3.model.factory;

import org.springframework.stereotype.Service;
import pl.kurs.javatestr3.commands.CreateShapeCommand;
import pl.kurs.javatestr3.model.factory.shape.ShapeCreator;
import pl.kurs.javatestr3.model.inheritance.Shape;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ShapeFactory {
    private final Map<String, ShapeCreator> creators;

    public ShapeFactory(Set<ShapeCreator> creators) {
        this.creators = creators.stream().collect(Collectors.toMap(ShapeCreator::getType, Function.identity()));
    }

    public Shape create(CreateShapeCommand command) {
        return creators.get(command.getShapeType()).create(command.getShapeType(), command.getParameters());
    }
}
