package pl.kurs.javatestr3.service;

import org.hibernate.StaleObjectStateException;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.javatestr3.commands.CreateShapeCommand;
import pl.kurs.javatestr3.commands.ShapeUpdateCommand;
import pl.kurs.javatestr3.exception.customexceptions.FigureCannotBeChangedException;
import pl.kurs.javatestr3.exception.customexceptions.FigureNotFoundException;
import pl.kurs.javatestr3.model.ShapeChange;
import pl.kurs.javatestr3.model.factory.ShapeFactory;
import pl.kurs.javatestr3.model.inheritance.Shape;
import pl.kurs.javatestr3.repository.AppUserRepository;
import pl.kurs.javatestr3.repository.ShapeChangeRepository;
import pl.kurs.javatestr3.repository.ShapeRepository;
import pl.kurs.javatestr3.security.AppUser;
import pl.kurs.javatestr3.service.shapespecification.ShapeSpecification;

import java.util.List;
import java.util.Map;

@Service
public class ShapeService {

    private final ShapeRepository repository;
    private final ShapeFactory shapeFactory;
    private final ShapeSpecification shapeSpecification;
    private final ShapeChangeService shapeChangeService;
    private final AppUserRepository appUserRepository;
    private final ShapeChangeRepository shapeChangeRepository;
    private final ModelMapper modelMapper;

    public ShapeService(ShapeRepository repository, ShapeFactory shapeFactory, ShapeSpecification shapeSpecification, ShapeChangeService shapeChangeService, AppUserRepository appUserRepository, ShapeChangeRepository shapeChangeRepository, ModelMapper modelMapper) {
        this.repository = repository;
        this.shapeFactory = shapeFactory;
        this.shapeSpecification = shapeSpecification;
        this.shapeChangeService = shapeChangeService;
        this.appUserRepository = appUserRepository;
        this.shapeChangeRepository = shapeChangeRepository;
        this.modelMapper = modelMapper;
    }

    public Shape updateShape(Long id, ShapeUpdateCommand command, AppUser currentUser) {
        Shape toEdit = repository.findById(id).orElseThrow(() -> new FigureNotFoundException("Figure not found"));
        Map<String, Object> changedFields = command.getParameters();
        List<ShapeChange> changes = shapeChangeService.getChanges(toEdit, changedFields, currentUser);

        for (Map.Entry<String, Object> entry : changedFields.entrySet()) {
            String fieldName = entry.getKey();
            Object newValue = entry.getValue();

            toEdit.setField(fieldName, newValue);
        }

        toEdit.setVersion(command.getVersion());

        try {
            toEdit = repository.save(toEdit);
        } catch (StaleObjectStateException e) {
            throw new FigureCannotBeChangedException("The figure is being modified by another user, please try again!", e);
        }

        shapeChangeRepository.saveAll(changes);
        return toEdit;
    }

    @Transactional
    public Shape createShape(CreateShapeCommand command) {
        return repository.saveAndFlush(shapeFactory.create(command));
    }

    @Transactional(readOnly = true)
    public List<Shape> getShapesByParameters(Map<String, String> queryParams) {
        Specification<Shape> spec = shapeSpecification.findByCriteria(queryParams);
        return repository.findAll(spec);
    }
}
