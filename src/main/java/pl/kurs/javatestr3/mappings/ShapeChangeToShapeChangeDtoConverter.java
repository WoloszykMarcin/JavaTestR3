package pl.kurs.javatestr3.mappings;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.javatestr3.dto.ShapeChangeDto;
import pl.kurs.javatestr3.model.ShapeChange;

@Service
public class ShapeChangeToShapeChangeDtoConverter implements Converter<ShapeChange, ShapeChangeDto> {

    @Override
    public ShapeChangeDto convert(MappingContext<ShapeChange, ShapeChangeDto> mappingContext) {
        ShapeChange source = mappingContext.getSource();
        ShapeChangeDto dto = ShapeChangeDto.builder()
                .id(source.getId())
                .shapeId(source.getShape().getId())
                .shapeType(source.getShape().getType())
                .modifiedField(source.getModifiedField())
                .oldValue(source.getOldValue())
                .newValue(source.getNewValue())
                .changeDate(source.getChangeDate())
                .modifiedByUsername(source.getModifiedBy().getUsername())
                .build();

        return dto;
    }
}
