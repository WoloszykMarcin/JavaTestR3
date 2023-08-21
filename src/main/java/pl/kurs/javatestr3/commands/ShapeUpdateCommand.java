package pl.kurs.javatestr3.commands;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class ShapeUpdateCommand {
    private Long id;
    private Map<String, Object> parameters;
}
