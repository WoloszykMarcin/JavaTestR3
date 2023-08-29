package pl.kurs.javatestr3.service.shapespecification;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class RectangleSpecificationTest {

    private RectangleSpecification rectangleSpecification;

    @BeforeEach
    public void setUp() {
        rectangleSpecification = new RectangleSpecification();
    }

    @Test
    public void supportedParametersTest() {
        List<String> expectedParameters = Arrays.asList("widthFrom", "widthTo", "lengthFrom", "lengthTo", "areaFrom", "areaTo", "perimeterFrom", "perimeterTo");
        Assertions.assertThat(rectangleSpecification.supportedParameters()).containsExactlyInAnyOrderElementsOf(expectedParameters);
    }
}