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
@DiscriminatorValue("SQUARE")
public class Square extends Shape {
    @Column(name = "side")
    private double side;

    public Square(String type, double side) {
        super(type);
        this.side = side;
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("side", side);
        return parameters;
    }
}
