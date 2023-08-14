package pl.kurs.javatestr3.service.shapespecification;

import pl.kurs.javatestr3.model.inheritance.Shape;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public interface ISpecification {
    List<String> supportedParameters();
    Predicate build(Root<Shape> root, CriteriaBuilder cb, String paramName, String paramValue);
}
