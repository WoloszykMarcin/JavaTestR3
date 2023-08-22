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
@NoArgsConstructor
@Entity
@DiscriminatorValue("RECTANGLE")
public class Rectangle extends Shape {

    @Column(name = "length")
    private double length;
    @Column(name = "width")
    private double width;

    public Rectangle(String type, double length, double width) {
        super(type);
        this.length = length;
        this.width = width;
    }

    @Override
    public double getPerimeter() {
        return 2 * length + 2 * width;
    }

    @Override
    public double getArea() {
        return length * width;
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("length", length);
        parameters.put("width", width);
        return parameters;
    }
}
