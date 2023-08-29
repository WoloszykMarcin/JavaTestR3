package pl.kurs.javatestr3.service.shapespecification;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class CircleSpecificationTest {

    private CircleSpecification circleSpecification;

    @BeforeEach
    public void setUp() {
        circleSpecification = new CircleSpecification();
    }

    @Test
    public void supportedParametersTest() {
        List<String> expectedParameters = Arrays.asList("radiusFrom", "radiusTo", "areaFrom", "areaTo", "perimeterFrom", "perimeterTo");
        Assertions.assertThat(circleSpecification.supportedParameters()).containsExactlyInAnyOrderElementsOf(expectedParameters);
    }
}