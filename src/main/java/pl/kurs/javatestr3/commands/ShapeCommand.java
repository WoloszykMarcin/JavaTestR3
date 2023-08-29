package pl.kurs.javatestr3.commands;

import java.util.Map;

public interface ShapeCommand {
    String getShapeType();
    Map<String, Object> getParameters();
}
