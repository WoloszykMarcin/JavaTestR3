package pl.kurs.javatestr3.service.shapespecification;

import org.springframework.stereotype.Component;
import pl.kurs.javatestr3.model.inheritance.Shape;
import pl.kurs.javatestr3.model.inheritance.Square;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class SquareSpecification implements ISpecification {

    @Override
    public List<String> supportedParameters() {
        return List.of("sideFrom", "sideTo");
    }

    @Override
    public Predicate build(Root<Shape> root, CriteriaBuilder cb, String paramName, String paramValue) {
        Root<Square> squareRoot = cb.treat(root, Square.class);
        switch (paramName) {
            case "sideFrom":
                return cb.greaterThanOrEqualTo(squareRoot.get("side"), Double.valueOf(paramValue));
            case "sideTo":
                return cb.lessThanOrEqualTo(squareRoot.get("side"), Double.valueOf(paramValue));
            default:
                return null;
        }
    }

}
