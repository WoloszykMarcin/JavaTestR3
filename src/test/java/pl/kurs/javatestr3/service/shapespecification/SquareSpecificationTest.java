package pl.kurs.javatestr3.service.shapespecification;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class SquareSpecificationTest {

    private SquareSpecification squareSpecification;

    @BeforeEach
    public void setUp() {
        squareSpecification = new SquareSpecification();
    }

    @Test
    public void supportedParametersTest() {
        List<String> expectedParameters = Arrays.asList("sideFrom", "sideTo", "areaFrom", "areaTo", "perimeterFrom", "perimeterTo");
        Assertions.assertThat(squareSpecification.supportedParameters()).containsExactlyInAnyOrderElementsOf(expectedParameters);
    }

}