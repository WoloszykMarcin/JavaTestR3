package pl.kurs.javatestr3.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class ShapeChangeDto {
    private Long id;
    private Long shapeId;
    private String shapeType;
    private String modifiedField;
    private String oldValue;
    private String newValue;
    private LocalDateTime changeDate;
    private String modifiedByUsername;
}