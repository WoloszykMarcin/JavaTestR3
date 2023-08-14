package pl.kurs.javatestr3.service.shapespecification;

import org.springframework.stereotype.Component;
import pl.kurs.javatestr3.model.inheritance.Rectangle;
import pl.kurs.javatestr3.model.inheritance.Shape;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class RectangleSpecification implements ISpecification {

    @Override
    public List<String> supportedParameters() {
        return List.of("widthFrom", "widthTo", "lengthFrom", "lengthTo");
    }

    @Override
    public Predicate build(Root<Shape> root, CriteriaBuilder cb, String paramName, String paramValue) {
        Root<Rectangle> rectangleRoot = cb.treat(root, Rectangle.class);
        switch (paramName) {
            case "widthFrom":
                return cb.greaterThanOrEqualTo(rectangleRoot.get("width"), Double.valueOf(paramValue));
            case "widthTo":
                return cb.lessThanOrEqualTo(rectangleRoot.get("width"), Double.valueOf(paramValue));
            case "lengthFrom":
                return cb.greaterThanOrEqualTo(rectangleRoot.get("length"), Double.valueOf(paramValue));
            case "lengthTo":
                return cb.lessThanOrEqualTo(rectangleRoot.get("length"), Double.valueOf(paramValue));
            default:
                return null;
        }
    }
}