package pl.kurs.javatestr3.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.javatestr3.model.ShapeChange;
import pl.kurs.javatestr3.model.inheritance.Shape;
import pl.kurs.javatestr3.repository.ShapeChangeRepository;
import pl.kurs.javatestr3.security.AppUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ShapeChangeService {

    private final ShapeChangeRepository shapeChangeRepository;

    public ShapeChangeService(ShapeChangeRepository shapeChangeRepository) {
        this.shapeChangeRepository = shapeChangeRepository;
    }

    @Transactional(readOnly = true)
    public List<ShapeChange> getChangesForShape(Long shapeId) {
        return shapeChangeRepository.findByShapeIdWithDetails(shapeId);
    }

    public List<ShapeChange> getChanges(Shape shape, Map<String, Object> changedFields, AppUser modifiedBy) {
        List<ShapeChange> changesList = new ArrayList<>();

        for (Map.Entry<String, Object> entry : changedFields.entrySet()) {
            String fieldName = entry.getKey();
            Object newValueObject = entry.getValue();

            String newValue = newValueObject != null ? newValueObject.toString() : null;
            String oldValue = getOldValue(shape, fieldName);

            if (hasValueChanged(newValue, oldValue) && shape.getParameters().containsKey(fieldName)) {
                ShapeChange change = new ShapeChange();
                change.setShape(shape);
                change.setModifiedField(fieldName);
                change.setOldValue(oldValue);
                change.setNewValue(newValue);
                change.setChangeDate(LocalDateTime.now());
                change.setModifiedBy(modifiedBy);

                changesList.add(change);
            }
        }

        return changesList;
    }

    private String getOldValue(Shape shape, String fieldName) {
        return Optional.ofNullable(shape.getParameters().get(fieldName)).map(Object::toString).orElse(null);
    }

    private boolean hasValueChanged(String newValue, String oldValue) {
        return !((newValue == null && oldValue == null) || (newValue != null && newValue.equals(oldValue)) || (oldValue != null && oldValue.equals(newValue)));
    }
}
