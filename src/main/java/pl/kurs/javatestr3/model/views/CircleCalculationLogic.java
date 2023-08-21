package pl.kurs.javatestr3.model.views;

import org.springframework.stereotype.Service;

@Service
public class CircleCalculationLogic implements ShapeCalculationLogic {

    @Override
    public String getAreaCalculationLogic() {
        return Math.PI + " * radius * radius";
    }

    @Override
    public String getPerimeterCalculationLogic() {
        return "2 * " + Math.PI + " * radius";
    }
}