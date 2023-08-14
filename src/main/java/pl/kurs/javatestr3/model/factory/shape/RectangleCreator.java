package pl.kurs.javatestr3.model.factory.shape;

import org.springframework.stereotype.Service;
import pl.kurs.javatestr3.model.inheritance.Rectangle;
import pl.kurs.javatestr3.model.inheritance.Shape;

import java.util.Map;

@Service
public class RectangleCreator implements ShapeCreator {
    @Override
    public String getType() {
        return "rectangle";
    }

    @Override
    public Shape create(String shapeType, Map<String, Object> parameters) {
        return new Rectangle(getType(),
                getDoubleParameter("length", parameters),
                getDoubleParameter("width", parameters));
    }
}
