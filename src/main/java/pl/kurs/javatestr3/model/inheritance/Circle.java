package pl.kurs.javatestr3.model.inheritance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Entity
@NoArgsConstructor
@DiscriminatorValue("CIRCLE")
public class Circle extends Shape {

    @Column(name = "radius")
    private double radius;

    public Circle(String type, double radius) {
        super(type);
        this.radius = radius;
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("radius", radius);
        return parameters;
    }
}
