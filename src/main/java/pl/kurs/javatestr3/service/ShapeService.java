package pl.kurs.javatestr3.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.javatestr3.commands.CreateShapeCommand;
import pl.kurs.javatestr3.commands.ShapeUpdateCommand;
import pl.kurs.javatestr3.dto.ShapeFullDto;
import pl.kurs.javatestr3.exception.customexceptions.FigureNotFoundException;
import pl.kurs.javatestr3.model.factory.ShapeFactory;
import pl.kurs.javatestr3.model.inheritance.Shape;
import pl.kurs.javatestr3.repository.AppUserRepository;
import pl.kurs.javatestr3.repository.ShapeRepository;
import pl.kurs.javatestr3.security.AppUser;
import pl.kurs.javatestr3.service.shapespecification.ShapeSpecification;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShapeService {

    private final ShapeRepository repository;
    private final ShapeFactory shapeFactory;
    private final ShapeSpecification shapeSpecification;
    private final ShapeChangeService shapeChangeService;

    private final AppUserRepository appUserRepository;
    private final ModelMapper modelMapper;

    public ShapeService(ShapeRepository repository, ShapeFactory shapeFactory, ShapeSpecification shapeSpecification, ShapeChangeService shapeChangeService, AppUserRepository appUserRepository, ModelMapper modelMapper) {
        this.repository = repository;
        this.shapeFactory = shapeFactory;
        this.shapeSpecification = shapeSpecification;
        this.shapeChangeService = shapeChangeService;
        this.appUserRepository = appUserRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void updateShape(Long id, ShapeUpdateCommand command, AppUser currentUser) {
        Shape toEdit = repository.findById(id).orElseThrow(() -> new FigureNotFoundException("Figure not found"));

        Map<String, Object> changedFields = command.getParameters();

        shapeChangeService.registerChange(toEdit, changedFields, currentUser);

        for (Map.Entry<String, Object> entry : changedFields.entrySet()) {
            String fieldName = entry.getKey();
            Object newValue = entry.getValue();

            updateShapeField(toEdit, fieldName, newValue);
        }

        repository.saveAndFlush(toEdit);
    }

    private void updateShapeField(Shape shape, String fieldName, Object newValue) {
        try {
            Field field = shape.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(shape, newValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error updating field", e);
        }
    }

    @Transactional
    public Shape createShape(CreateShapeCommand command, AppUser currentUser) {
        Shape savedShape = repository.saveAndFlush(shapeFactory.create(command));
        incrementUserFigureCount(currentUser);

        return savedShape;
    }

    @Transactional
    void incrementUserFigureCount(AppUser user) {
        user.setNumberOfCreatedFigures(user.getNumberOfCreatedFigures() + 1);
        appUserRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<ShapeFullDto> getShapesByParameters(Map<String, String> queryParams) {
        Specification<Shape> spec = shapeSpecification.findByCriteria(queryParams);
        List<Shape> shapes = repository.findAll(spec);

        shapes = filterShapesByAreaAndPerimeter(shapes, queryParams);

        return shapes.stream()
                .map(shape -> {
                    ShapeFullDto shapeFullDto = modelMapper.map(shape, ShapeFullDto.class);
                    shapeFullDto.setShapeType(shape.getType());
                    shapeFullDto.setCalculatedArea(shape.getArea());
                    shapeFullDto.setCalculatedPerimeter(shape.getPerimeter());
                    return shapeFullDto;
                })
                .collect(Collectors.toList());
    }

    private List<Shape> filterShapesByAreaAndPerimeter(List<Shape> shapes, Map<String, String> queryParams) {
        Double minArea = queryParams.containsKey("areaFrom") ? Double.valueOf(queryParams.get("areaFrom")) : null;
        Double maxArea = queryParams.containsKey("areaTo") ? Double.valueOf(queryParams.get("areaTo")) : null;
        Double minPerimeter = queryParams.containsKey("perimeterFrom") ? Double.valueOf(queryParams.get("perimeterFrom")) : null;
        Double maxPerimeter = queryParams.containsKey("perimeterTo") ? Double.valueOf(queryParams.get("perimeterTo")) : null;

        return shapes.stream()
                .filter(shape -> (minArea == null || shape.getArea() >= minArea)
                        && (maxArea == null || shape.getArea() <= maxArea)
                        && (minPerimeter == null || shape.getPerimeter() >= minPerimeter)
                        && (maxPerimeter == null || shape.getPerimeter() <= maxPerimeter))
                .collect(Collectors.toList());
    }
}
