package pl.kurs.javatestr3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.javatestr3.model.ShapeChange;

import java.util.List;

public interface ShapeChangeRepository extends JpaRepository<ShapeChange, Long> {

    @Query("SELECT sc FROM ShapeChange sc JOIN FETCH sc.shape s JOIN FETCH sc.modifiedBy m WHERE s.id = :shapeId")
    List<ShapeChange> findByShapeIdWithDetails(Long shapeId);
}
