package pl.kurs.javatestr3.service.shapespecification;

import org.springframework.stereotype.Component;
import pl.kurs.javatestr3.model.inheritance.Circle;
import pl.kurs.javatestr3.model.inheritance.Shape;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class CircleSpecification implements ISpecification {

    @Override
    public List<String> supportedParameters() {
        return List.of("radiusFrom", "radiusTo", "areaFrom", "areaTo", "perimeterFrom", "perimeterTo");
    }

    @Override
    public Predicate build(Root<Shape> root, CriteriaBuilder cb, String paramName, String paramValue) {
        Root<Circle> circleRoot = cb.treat(root, Circle.class);

        Expression<Number> radius = circleRoot.get("radius").as(Number.class);
        Expression<Number> areaExpression = cb.prod(cb.literal(Math.PI), cb.prod(radius, radius));
        Expression<Number> perimeterExpression = cb.prod(cb.literal(2 * Math.PI), radius);

        switch (paramName) {
            case "radiusFrom":
                return cb.greaterThanOrEqualTo(circleRoot.get("radius"), Double.valueOf(paramValue));
            case "radiusTo":
                return cb.lessThanOrEqualTo(circleRoot.get("radius"), Double.valueOf(paramValue));
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