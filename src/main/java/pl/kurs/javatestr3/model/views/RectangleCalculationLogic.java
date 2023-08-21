package pl.kurs.javatestr3.model.views;

import org.springframework.stereotype.Service;

@Service
public class RectangleCalculationLogic implements ShapeCalculationLogic {

    @Override
    public String getAreaCalculationLogic() {
        return "length * width";
    }

    @Override
    public String getPerimeterCalculationLogic() {
        return "2 * (length + width)";
    }
}
