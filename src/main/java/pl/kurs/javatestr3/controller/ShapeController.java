package pl.kurs.javatestr3.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.javatestr3.commands.CreateShapeCommand;
import pl.kurs.javatestr3.model.inheritance.Shape;
import pl.kurs.javatestr3.service.ShapeService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/shapes")
public class ShapeController {

    private final ShapeService shapeService;

    public ShapeController(ShapeService shapeService) {
        this.shapeService = shapeService;
    }

    @PostMapping
    public ResponseEntity<Shape> save(@RequestBody @Valid CreateShapeCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shapeService.createShape(command));
    }

    @GetMapping
    public ResponseEntity<List<Shape>> getShapesByParameters(@RequestParam Map<String, String> queryParams) {
        List<Shape> shapes = shapeService.getShapesByParameters(queryParams);

        return shapes.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(shapes);
    }
}
