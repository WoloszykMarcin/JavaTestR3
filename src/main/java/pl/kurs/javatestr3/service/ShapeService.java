package pl.kurs.javatestr3.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.javatestr3.commands.CreateShapeCommand;
import pl.kurs.javatestr3.model.factory.ShapeFactory;
import pl.kurs.javatestr3.model.inheritance.Shape;
import pl.kurs.javatestr3.repository.ShapeRepository;
import pl.kurs.javatestr3.service.shapespecification.ShapeSpecification;

import java.util.List;
import java.util.Map;

@Service
public class ShapeService {

    private final ShapeRepository repository;

    private final ShapeFactory shapeFactory;
    private final ShapeSpecification shapeSpecification;

    public ShapeService(ShapeRepository repository, ShapeFactory shapeFactory, ShapeSpecification shapeSpecification) {
        this.repository = repository;
        this.shapeFactory = shapeFactory;
        this.shapeSpecification = shapeSpecification;
    }

    @Transactional(readOnly = true)
    public List<Shape> findAll() {
        return repository.findAll();
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
