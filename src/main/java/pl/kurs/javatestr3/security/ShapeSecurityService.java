package pl.kurs.javatestr3.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pl.kurs.javatestr3.repository.ShapeRepository;

@Service("shapeSecurityService")
public class ShapeSecurityService {

    private final ShapeRepository shapeRepository;

    public ShapeSecurityService(ShapeRepository shapeRepository) {
        this.shapeRepository = shapeRepository;
    }

    public boolean isShapeOwner(Long shapeId, Authentication authentication) {
        String currentUsername = authentication.getName();
        return shapeRepository.existsByCreatedByUsernameAndId(currentUsername, shapeId);
    }
}
