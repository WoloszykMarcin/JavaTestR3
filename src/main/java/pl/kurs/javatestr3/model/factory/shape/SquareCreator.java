package pl.kurs.javatestr3.model.factory.shape;

import org.springframework.stereotype.Service;
import pl.kurs.javatestr3.model.inheritance.Shape;
import pl.kurs.javatestr3.model.inheritance.Square;

import java.util.Map;

@Service
public class SquareCreator implements ShapeCreator {
    @Override
    public String getType() {
        return "square";
    }

    @Override
    public Shape create(String shapeType, Map<String, Object> parameters) {
        return new Square(getType(), getDoubleParameter("side", parameters));
    }
}
