package pl.kurs.javatestr3.model.inheritance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import static java.lang.Math.PI;

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
    public double calculatePerimeter() {
        return 2 * PI * radius;
    }

    @Override
    public double calculateArea() {
        return PI * Math.pow(radius, 2);
    }
}
