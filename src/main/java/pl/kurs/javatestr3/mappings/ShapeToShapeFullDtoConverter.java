package pl.kurs.javatestr3.mappings;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.javatestr3.dto.ShapeFullDto;
import pl.kurs.javatestr3.model.inheritance.Shape;
import pl.kurs.javatestr3.service.CalculationService;

@Service
public class ShapeToShapeFullDtoConverter implements Converter<Shape, ShapeFullDto> {

    private final CalculationService calculationService;

    public ShapeToShapeFullDtoConverter(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @Override
    public ShapeFullDto convert(MappingContext<Shape, ShapeFullDto> mappingContext) {
        Shape source = mappingContext.getSource();

        return ShapeFullDto.builder()
                .id(source.getId())
                .shapeType(source.getType())
                .createdDate(source.getCreatedDate())
                .createdBy(source.getCreatedBy().getUsername())
                .lastModifiedDate(source.getLastModifiedDate())
                .lastModifiedBy(source.getLastModifiedBy().getUsername())
                .parameters(source.getParameters())
                .calculatedArea(calculationService.fetchCalculatedAreaFromView(source.getType(), source.getId()))
                .calculatedPerimeter(calculationService.fetchCalculatedPerimeterFromView(source.getType(), source.getId()))
                .build();
    }
}
