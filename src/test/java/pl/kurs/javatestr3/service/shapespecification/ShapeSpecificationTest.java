package pl.kurs.javatestr3.service.shapespecification;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.kurs.javatestr3.exception.customexceptions.InvalidDateFormatException;
import pl.kurs.javatestr3.exception.customexceptions.WrongAttributeException;
import pl.kurs.javatestr3.model.inheritance.Shape;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class ShapeSpecificationTest {

    @Mock
    private CriteriaBuilder mockCb;

    @Mock
    private Root<Shape> mockRoot;

    @Mock
    private Predicate mockPredicate;

    @InjectMocks
    private ShapeSpecification shapeSpecification;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        shapeSpecification = new ShapeSpecification(Arrays.asList(new CircleSpecification(), new RectangleSpecification(), new SquareSpecification()));
    }

    @Test
    public void circleSpecificationSupportedParametersTest() {
        //given
        CircleSpecification circleSpec = new CircleSpecification();
        //when
        List<String> parameters = circleSpec.supportedParameters();
        //then
        assertTrue(parameters.contains("radiusFrom"));
        assertTrue(parameters.contains("radiusTo"));
    }

    @Test
    public void rectangleSpecificationSupportedParametersTest() {
        //given
        RectangleSpecification rectangleSpec = new RectangleSpecification();
        //when
        List<String> parameters = rectangleSpec.supportedParameters();
        //then
        assertTrue(parameters.contains("widthFrom"));
        assertTrue(parameters.contains("widthTo"));
        assertTrue(parameters.contains("lengthFrom"));
        assertTrue(parameters.contains("lengthTo"));
    }

    @Test
    public void squareSpecificationSupportedParametersTest() {
        //given
        SquareSpecification squareSpec = new SquareSpecification();
        //when
        List<String> parameters = squareSpec.supportedParameters();
        //then
        assertTrue(parameters.contains("sideFrom"));
        assertTrue(parameters.contains("sideTo"));
    }

    @Test
    public void findByCriteriaUnrecognizedParameterTest() {
        //given
        Map<String, String> criteria = new HashMap<>();
        criteria.put("unrecognizedParameter", "value");

        //when
        Specification<Shape> spec = shapeSpecification.findByCriteria(criteria);

        //then
        assertThrows(WrongAttributeException.class, () -> spec.toPredicate(mockRoot, null, mockCb));
    }

    @Test
    public void findByCriteriaWithInvalidDateTest() {
        //given
        Map<String, String> criteria = new HashMap<>();
        criteria.put("createdAtFrom", "99999-01-01T12:00:00");

        //when
        Specification<Shape> spec = shapeSpecification.findByCriteria(criteria);

        //then
        assertThrows(InvalidDateFormatException.class, () -> spec.toPredicate(mockRoot, null, mockCb));
    }

    @Test
    public void findByCriteriaWithValidDateTest() {
        //given
        Map<String, String> criteria = new HashMap<>();
        criteria.put("createdAtFrom", "2023-01-01T12:00:00");

        //when
        when(mockCb.greaterThanOrEqualTo(any(), any(LocalDateTime.class))).thenReturn(mockPredicate);

        Specification<Shape> spec = shapeSpecification.findByCriteria(criteria);
        spec.toPredicate(mockRoot, null, mockCb);

        //then
        verify(mockCb).greaterThanOrEqualTo(any(), any(LocalDateTime.class));
    }

    @Test
    public void findByCriteriaWithValidTypeTest() {
        //given
        Map<String, String> criteria = new HashMap<>();
        criteria.put("type", "CIRCLE");

        //when
        when(mockCb.greaterThanOrEqualTo(any(), anyDouble())).thenReturn(mockPredicate);

        Specification<Shape> spec = shapeSpecification.findByCriteria(criteria);
        spec.toPredicate(mockRoot, null, mockCb);

        //then
        verify(mockCb).equal(mockRoot.get("type"), "CIRCLE");
    }

    @Test
    public void findByCriteriaWithInvalidTypeTest() {
        //given
        Map<String, String> criteria = new HashMap<>();
        criteria.put("wrongType", "CIRCLE");

        //when
        when(mockCb.greaterThanOrEqualTo(any(), anyDouble())).thenReturn(mockPredicate);

        Specification<Shape> spec = shapeSpecification.findByCriteria(criteria);

        //then
        assertThrows(WrongAttributeException.class, () -> spec.toPredicate(mockRoot, null, mockCb));
    }
}
