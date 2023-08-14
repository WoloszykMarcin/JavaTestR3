package pl.kurs.javatestr3.model.factory.shape;

import org.springframework.stereotype.Service;
import pl.kurs.javatestr3.model.inheritance.Circle;
import pl.kurs.javatestr3.model.inheritance.Shape;

import java.util.Map;

@Service
public class CircleCreator implements ShapeCreator {
    @Override
    public String getType() {
        return "circle";
    }

    @Override
    public Shape create(String shapeType, Map<String, Object> parameters) {
        return new Circle(getType(),
                getDoubleParameter("radius", parameters));
    }
}
