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
    private final CalculationService calculationService;
    private final ModelMapper modelMapper;

    public ShapeService(ShapeRepository repository, ShapeFactory shapeFactory, ShapeSpecification shapeSpecification, ShapeChangeService shapeChangeService, CalculationService calculationService, ModelMapper modelMapper) {
        this.repository = repository;
        this.shapeFactory = shapeFactory;
        this.shapeSpecification = shapeSpecification;
        this.shapeChangeService = shapeChangeService;
        this.calculationService = calculationService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void updateShape(Long id, ShapeUpdateCommand command, AppUser currentUser) {
        Shape toEdit = repository.findById(id).orElseThrow(() -> new FigureNotFoundException("Figure not found"));
        toEdit.setLastModifiedBy(currentUser);

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
    public Shape createShape(CreateShapeCommand command) {
        return repository.saveAndFlush(shapeFactory.create(command));
    }

    @Transactional(readOnly = true)
    public List<ShapeFullDto> getShapesByParameters(Map<String, String> queryParams) {
        Specification<Shape> spec = shapeSpecification.findByCriteria(queryParams);
        List<Shape> shapes = repository.findAll(spec);

        //temporary used as an addon to ShapeToShapeFullDtoConverter (issues with getType() and calculating area and perimeter (manual set up works fine))
        return shapes.stream()
                .map(shape -> setTypePerimeterAndArea(shape, modelMapper.map(shape, ShapeFullDto.class)))
                .collect(Collectors.toList());
    }

    //temporary used as an addon to ShapeToShapeFullDtoConverter (issues with getType() and calculating area and perimeter (manual set up works fine))
    public ShapeFullDto setTypePerimeterAndArea(Shape shape, ShapeFullDto shapeFullDto) {
        //set type
        shapeFullDto.setShapeType(shape.getType());

        //get calculated area and perimeter
        Double calculatedArea = calculationService.fetchCalculatedAreaFromView(shape.getType(), shape.getId());
        Double calculatedPerimeter = calculationService.fetchCalculatedPerimeterFromView(shape.getType(), shape.getId());

        shapeFullDto.setCalculatedArea(calculatedArea);
        shapeFullDto.setCalculatedPerimeter(calculatedPerimeter);

        return shapeFullDto;
    }
}
