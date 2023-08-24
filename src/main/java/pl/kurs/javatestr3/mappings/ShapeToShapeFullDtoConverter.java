package pl.kurs.javatestr3.mappings;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.javatestr3.dto.ShapeFullDto;
import pl.kurs.javatestr3.model.inheritance.Shape;

@Service
public class ShapeToShapeFullDtoConverter implements Converter<Shape, ShapeFullDto> {

    @Override
    public ShapeFullDto convert(MappingContext<Shape, ShapeFullDto> mappingContext) {
        Shape source = mappingContext.getSource();

        ShapeFullDto dto = ShapeFullDto.builder()
                .id(source.getId())
                .type(source.getType())
                .version(source.getVersion())
                .createdDate(source.getCreatedDate())
                .createdBy(source.getCreatedBy().getUsername())
                .lastModifiedDate(source.getLastModifiedDate())
                .lastModifiedBy(source.getLastModifiedBy().getUsername())
                .parameters(source.getParameters())
                .calculatedArea(source.getCalculatedArea())
                .calculatedPerimeter(source.getCalculatedPerimeter())
                .build();

        return dto;
    }
}
