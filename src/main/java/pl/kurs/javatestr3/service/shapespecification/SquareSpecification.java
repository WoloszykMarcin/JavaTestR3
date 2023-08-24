package pl.kurs.javatestr3.service.shapespecification;

import org.springframework.stereotype.Component;
import pl.kurs.javatestr3.model.inheritance.Shape;
import pl.kurs.javatestr3.model.inheritance.Square;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class SquareSpecification implements ISpecification {

    @Override
    public List<String> supportedParameters() {
        return List.of("sideFrom", "sideTo", "areaFrom", "areaTo", "perimeterFrom", "perimeterTo");
    }

    @Override
    public Predicate build(Root<Shape> root, CriteriaBuilder cb, String paramName, String paramValue) {
        Root<Square> squareRoot = cb.treat(root, Square.class);

        Expression<Number> side = squareRoot.get("side").as(Number.class);
        Expression<Number> areaExpression = cb.prod(side, side);
        Expression<Number> perimeterExpression = cb.prod(cb.literal(4.0), side);

        switch (paramName) {
            case "sideFrom":
                return cb.greaterThanOrEqualTo(squareRoot.get("side"), Double.valueOf(paramValue));
            case "sideTo":
                return cb.lessThanOrEqualTo(squareRoot.get("side"), Double.valueOf(paramValue));
            case "areaFrom":
                return cb.greaterThanOrEqualTo(areaExpression.as(Double.class), Double.valueOf(paramValue));
            case "areaTo":
                return cb.lessThanOrEqualTo(areaExpression.as(Double.class), Double.valueOf(paramValue));
            case "perimeterFrom":
                return cb.greaterThanOrEqualTo(perimeterExpression.as(Double.class), Double.valueOf(paramValue));
            case "perimeterTo":
                return cb.lessThanOrEqualTo(perimeterExpression.as(Double.class), Double.valueOf(paramValue));
            default:
                return null;
        }
    }
}
