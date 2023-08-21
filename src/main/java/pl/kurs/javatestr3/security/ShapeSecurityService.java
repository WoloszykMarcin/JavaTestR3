package pl.kurs.javatestr3.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pl.kurs.javatestr3.exception.customexceptions.FigureNotFoundException;
import pl.kurs.javatestr3.model.inheritance.Shape;
import pl.kurs.javatestr3.repository.ShapeRepository;

@Service("shapeSecurityService")
public class ShapeSecurityService {

    private final ShapeRepository shapeRepository;

    public ShapeSecurityService(ShapeRepository shapeRepository) {
        this.shapeRepository = shapeRepository;
    }

    public boolean isShapeOwner(Long shapeId, Authentication authentication) {
        String currentUsername = authentication.getName();

        Shape shape = shapeRepository.findById(shapeId)
                .orElseThrow(() -> new FigureNotFoundException("Shape with id " + shapeId + " not found."));

        return shape.getCreatedBy().getUsername().equals(currentUsername);
    }
}
