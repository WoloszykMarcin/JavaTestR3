package pl.kurs.javatestr3.model.views;

import org.springframework.stereotype.Service;

@Service
public class SquareCalculationLogic implements ShapeCalculationLogic {

    @Override
    public String getAreaCalculationLogic() {
        return "side * side";
    }

    @Override
    public String getPerimeterCalculationLogic() {
        return "4 * side";
    }
}

