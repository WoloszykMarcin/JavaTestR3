package pl.kurs.javatestr3.dto;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShapeFullDto {
    private Long id;
    private String shapeType;
    private int version;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime lastModifiedDate;
    private String lastModifiedBy;
    private Map<String, Object> parameters;
    private double calculatedArea;
    private double calculatedPerimeter;
}
