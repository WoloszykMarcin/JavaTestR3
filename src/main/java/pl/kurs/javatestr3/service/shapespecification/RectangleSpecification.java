package pl.kurs.javatestr3.service.shapespecification;

import org.springframework.stereotype.Component;
import pl.kurs.javatestr3.model.inheritance.Rectangle;
import pl.kurs.javatestr3.model.inheritance.Shape;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class RectangleSpecification implements ISpecification {

    @Override
    public List<String> supportedParameters() {
        return List.of("widthFrom", "widthTo", "lengthFrom", "lengthTo", "areaFrom", "areaTo", "perimeterFrom", "perimeterTo");
    }

    @Override
    public Predicate build(Root<Shape> root, CriteriaBuilder cb, String paramName, String paramValue) {
        Root<Rectangle> rectangleRoot = cb.treat(root, Rectangle.class);

        Expression<Number> width = rectangleRoot.get("width").as(Number.class);
        Expression<Number> length = rectangleRoot.get("length").as(Number.class);
        Expression<Number> areaExpression = cb.prod(length, width);
        Expression<Number> perimeterExpression = cb.prod(cb.literal(2.0), cb.sum(length, width));

        switch (paramName) {
            case "widthFrom":
                return cb.greaterThanOrEqualTo(rectangleRoot.get("width"), Double.valueOf(paramValue));
            case "widthTo":
                return cb.lessThanOrEqualTo(rectangleRoot.get("width"), Double.valueOf(paramValue));
            case "lengthFrom":
                return cb.greaterThanOrEqualTo(rectangleRoot.get("length"), Double.valueOf(paramValue));
            case "lengthTo":
                return cb.lessThanOrEqualTo(rectangleRoot.get("length"), Double.valueOf(paramValue));
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