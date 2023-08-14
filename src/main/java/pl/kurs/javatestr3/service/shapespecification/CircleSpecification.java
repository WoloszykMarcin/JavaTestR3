package pl.kurs.javatestr3.service.shapespecification;

import org.springframework.stereotype.Component;
import pl.kurs.javatestr3.model.inheritance.Circle;
import pl.kurs.javatestr3.model.inheritance.Shape;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class CircleSpecification implements ISpecification {
    @Override
    public List<String> supportedParameters() {
        return List.of("radiusFrom", "radiusTo");
    }

    @Override
    public Predicate build(Root<Shape> root, CriteriaBuilder cb, String paramName, String paramValue) {
        Root<Circle> circleRoot = cb.treat(root, Circle.class);
        switch (paramName) {
            case "radiusFrom":
                return cb.greaterThanOrEqualTo(circleRoot.get("radius"), Double.valueOf(paramValue));
            case "radiusTo":
                return cb.lessThanOrEqualTo(circleRoot.get("radius"), Double.valueOf(paramValue));
            default:
                return null;
        }
    }
}