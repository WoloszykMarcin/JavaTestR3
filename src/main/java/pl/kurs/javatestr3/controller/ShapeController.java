package pl.kurs.javatestr3.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pl.kurs.javatestr3.commands.CreateShapeCommand;
import pl.kurs.javatestr3.commands.ShapeUpdateCommand;
import pl.kurs.javatestr3.dto.ShapeChangeDto;
import pl.kurs.javatestr3.dto.ShapeFullDto;
import pl.kurs.javatestr3.model.ShapeChange;
import pl.kurs.javatestr3.model.inheritance.Shape;
import pl.kurs.javatestr3.security.AppUser;
import pl.kurs.javatestr3.service.AppUserService;
import pl.kurs.javatestr3.service.CalculationService;
import pl.kurs.javatestr3.service.ShapeChangeService;
import pl.kurs.javatestr3.service.ShapeService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/shapes")
public class ShapeController {

    private final ShapeChangeService shapeChangeService;
    private final ShapeService shapeService;

    private final AppUserService userService;

    private final ModelMapper modelMapper;

    public ShapeController(ShapeChangeService shapeChangeService, ShapeService shapeService, AppUserService userService, ModelMapper modelMapper, CalculationService calculationService) {
        this.shapeChangeService = shapeChangeService;
        this.shapeService = shapeService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<ShapeFullDto> save(@RequestBody @Valid CreateShapeCommand command) {
        Shape shape = shapeService.createShape(command);
        ShapeFullDto shapeFullDto = modelMapper.map(shape, ShapeFullDto.class);

        //temporary used as an addon to ShapeToShapeFullDtoConverter (issues with getType() and calculating area and perimeter (manual set up works fine))
        shapeFullDto = shapeService.setTypePerimeterAndArea(shape, shapeFullDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(shapeFullDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @shapeSecurityService.isShapeOwner(#id, authentication)")
    public ResponseEntity<Shape> updateShape(@PathVariable Long id, @RequestBody ShapeUpdateCommand command, Principal principal) {
        command.setId(id);
        String currentUsername = principal.getName();

        UserDetails userDetails = userService.loadUserByUsername(currentUsername);
        AppUser currentUser = (AppUser) userDetails;

        shapeService.updateShape(id, command, currentUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ShapeFullDto>> getShapesByParameters(@RequestParam Map<String, String> queryParams) {
        List<ShapeFullDto> shapeDtos = shapeService.getShapesByParameters(queryParams);

        return shapeDtos.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(shapeDtos);
    }

    @GetMapping("/{id}/changes")
    @PreAuthorize("hasRole('ADMIN') or @shapeSecurityService.isShapeOwner(#id, authentication)")
    public ResponseEntity<List<ShapeChangeDto>> getShapeChanges(@PathVariable Long id) {
        List<ShapeChange> changes = shapeChangeService.getChangesForShape(id);

        List<ShapeChangeDto> changesDto = changes.stream()
                .map(change -> modelMapper.map(change, ShapeChangeDto.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(changesDto);
    }
}
