package pl.kurs.javatestr3.service.shapespecification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.kurs.javatestr3.exception.InvalidDateFormatException;
import pl.kurs.javatestr3.model.inheritance.Shape;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShapeSpecification {
    private final Map<String, ISpecification> shapeParametersRegistry = new HashMap<>();

    private final List<ISpecification> shapeParameters;

    @Autowired
    public ShapeSpecification(List<ISpecification> shapeParameters) {
        this.shapeParameters = shapeParameters;
    }

    @PostConstruct
    private void initializeRegistry() {
        for (ISpecification param : shapeParameters) {
            for (String key : param.supportedParameters()) {
                shapeParametersRegistry.put(key, param);
            }
        }
    }

    public Specification<Shape> findByCriteria(Map<String, String> criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            for (Map.Entry<String, String> entry : criteria.entrySet()) {
                String paramName = entry.getKey();
                String paramValue = entry.getValue();

                ISpecification parameterBuilder = shapeParametersRegistry.get(paramName);
                if (parameterBuilder != null) {
                    Predicate customPredicate = parameterBuilder.build(root, cb, paramName, paramValue);
                    if (customPredicate != null) {
                        predicates.add(customPredicate);
                        continue;
                    }
                }

                try {
                    Predicate basePredicate = buildBasePredicate(root, cb, paramName, paramValue);
                    if (basePredicate != null) {
                        predicates.add(basePredicate);
                    }
                } catch (IllegalArgumentException ex) {
                    System.out.println("ex.getMessage() = " + ex.getMessage());
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Predicate buildBasePredicate(Root<Shape> root, CriteriaBuilder cb, String paramName, String paramValue) {

        switch (paramName) {
            case "type":
                return cb.equal(root.get("type"), paramValue);
            case "createdBy":
                return cb.equal(root.get("createdBy"), paramValue);
            case "areaFrom":
                return cb.greaterThanOrEqualTo(root.get("area"), Double.valueOf(paramValue));
            case "areaTo":
                return cb.lessThanOrEqualTo(root.get("area"), Double.valueOf(paramValue));
            case "perimeterFrom":
                return cb.greaterThanOrEqualTo(root.get("perimeter"), Double.valueOf(paramValue));
            case "perimeterTo":
                return cb.lessThanOrEqualTo(root.get("perimeter"), Double.valueOf(paramValue));
            case "createdAtFrom":
                LocalDateTime dateFrom = parseDate(paramValue);
                return cb.greaterThanOrEqualTo(root.get("createdDate"), dateFrom);
            case "createdAtTo":
                LocalDateTime dateTo = parseDate(paramValue);
                return cb.lessThanOrEqualTo(root.get("createdDate"), dateTo);
            default:
                throw new IllegalArgumentException("Attribute " + paramName + " is not recognized.");
        }
    }

    private static LocalDateTime parseDate(String dateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            return LocalDateTime.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException("Invalid date format");
        }
    }
}