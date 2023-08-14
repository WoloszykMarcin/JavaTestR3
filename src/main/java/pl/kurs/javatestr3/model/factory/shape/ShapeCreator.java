package pl.kurs.javatestr3.model.factory.shape;

import pl.kurs.javatestr3.model.inheritance.Shape;

import java.util.Map;

public interface ShapeCreator {
    String getType();

    Shape create(String shapeType, Map<String, Object> parameters);

    default Double getDoubleParameter(String name, Map<String, Object> parameters) {
        return (Double) parameters.get(name);
    }
}
