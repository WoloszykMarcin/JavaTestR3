package pl.kurs.javatestr3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.kurs.javatestr3.model.inheritance.Shape;

public interface ShapeRepository extends JpaRepository<Shape, Long>, JpaSpecificationExecutor<Shape> {
}
