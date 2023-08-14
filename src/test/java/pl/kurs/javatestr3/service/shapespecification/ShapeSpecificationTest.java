package pl.kurs.javatestr3.service.shapespecification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.kurs.javatestr3.model.inheritance.Circle;
import pl.kurs.javatestr3.model.inheritance.Rectangle;
import pl.kurs.javatestr3.model.inheritance.Shape;
import pl.kurs.javatestr3.model.inheritance.Square;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class ShapeSpecificationTest {

    @Autowired
    private ShapeSpecification shapeSpecification;

    private CriteriaBuilder mockCb;
    private Root<Shape> mockRoot;
    private Predicate mockPredicate;

    @BeforeEach
    public void setUp() {
        mockCb = mock(CriteriaBuilder.class);
        mockRoot = mock(Root.class);
        mockPredicate = mock(Predicate.class);
    }

    @Test
    public void circleSpecificationSupportedParametersTest() {
        CircleSpecification circleSpec = new CircleSpecification();
        List<String> parameters = circleSpec.supportedParameters();
        assertTrue(parameters.contains("radiusFrom"));
        assertTrue(parameters.contains("radiusTo"));
    }

    @Test
    public void rectangleSpecificationSupportedParametersTest() {
        RectangleSpecification rectangleSpec = new RectangleSpecification();
        List<String> parameters = rectangleSpec.supportedParameters();
        assertTrue(parameters.contains("widthFrom"));
        assertTrue(parameters.contains("widthTo"));
        assertTrue(parameters.contains("lengthFrom"));
        assertTrue(parameters.contains("lengthTo"));
    }

    @Test
    public void squareSpecificationSupportedParametersTest() {
        SquareSpecification squareSpec = new SquareSpecification();
        List<String> parameters = squareSpec.supportedParameters();
        assertTrue(parameters.contains("sideFrom"));
        assertTrue(parameters.contains("sideTo"));
    }

    @Test
    public void findByCriteriaUsingCircleSpecificationTest() {
        Map<String, String> criteria = new HashMap<>();
        criteria.put("radiusFrom", "5.0");

        when(mockCb.treat(mockRoot, Circle.class)).thenReturn(mock(Root.class));
        when(mockCb.greaterThanOrEqualTo(any(), anyDouble())).thenReturn(mockPredicate);

        Specification<Shape> spec = shapeSpecification.findByCriteria(criteria);
        spec.toPredicate(mockRoot, null, mockCb);

        verify(mockCb).treat(mockRoot, Circle.class);
        verify(mockCb).greaterThanOrEqualTo(any(), anyDouble());
    }

    @Test
    public void findByCriteriaUnrecognizedParameterTest() {
        Map<String, String> criteria = new HashMap<>();
        criteria.put("unrecognizedParameter", "value");

        Specification<Shape> spec = shapeSpecification.findByCriteria(criteria);
        Predicate result = spec.toPredicate(mockRoot, null, mockCb);

        assertNull(result);
    }

    @Test
    public void findByCriteriaUsingSquareSpecificationTest() {
        Map<String, String> criteria = new HashMap<>();
        criteria.put("sideFrom", "3.0");

        when(mockCb.treat(mockRoot, Square.class)).thenReturn(mock(Root.class));
        when(mockCb.greaterThanOrEqualTo(any(), anyDouble())).thenReturn(mockPredicate);

        Specification<Shape> spec = shapeSpecification.findByCriteria(criteria);
        spec.toPredicate(mockRoot, null, mockCb);

        verify(mockCb).treat(mockRoot, Square.class);
        verify(mockCb).greaterThanOrEqualTo(any(), anyDouble());
    }

    @Test
    public void findByCriteriaUsingRectangleSpecificationTest() {
        Map<String, String> criteria = new HashMap<>();
        criteria.put("widthFrom", "4.0");

        when(mockCb.treat(mockRoot, Rectangle.class)).thenReturn(mock(Root.class));
        when(mockCb.greaterThanOrEqualTo(any(), anyDouble())).thenReturn(mockPredicate);

        Specification<Shape> spec = shapeSpecification.findByCriteria(criteria);
        spec.toPredicate(mockRoot, null, mockCb);

        verify(mockCb).treat(mockRoot, Rectangle.class);
        verify(mockCb).greaterThanOrEqualTo(any(), anyDouble());
    }

    @Test
    public void findByCriteriaWithValidDateTest() {
        Map<String, String> criteria = new HashMap<>();
        criteria.put("createdAtFrom", "2023-01-01T12:00:00");

        when(mockCb.greaterThanOrEqualTo(any(), any(LocalDateTime.class))).thenReturn(mockPredicate);

        Specification<Shape> spec = shapeSpecification.findByCriteria(criteria);
        spec.toPredicate(mockRoot, null, mockCb);

        verify(mockCb).greaterThanOrEqualTo(any(), any(LocalDateTime.class));
    }
}
