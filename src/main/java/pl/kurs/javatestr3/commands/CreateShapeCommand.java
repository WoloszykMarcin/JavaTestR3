package pl.kurs.javatestr3.commands;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.kurs.javatestr3.validations.ValidShapeCommand;

import java.util.Map;

@Getter
@Setter
@ToString
@ValidShapeCommand
public class CreateShapeCommand {
    private String shapeType;
    private Map<String, Object> parameters;
}
