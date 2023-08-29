package pl.kurs.javatestr3.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;
import pl.kurs.javatestr3.JavaTestR3Application;
import pl.kurs.javatestr3.commands.CreateShapeCommand;
import pl.kurs.javatestr3.commands.ShapeUpdateCommand;
import pl.kurs.javatestr3.model.inheritance.Shape;
import pl.kurs.javatestr3.security.AppUser;

import java.util.Map;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


@SpringBootTest(classes = JavaTestR3Application.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
public class ShapeServiceTest {

    @Autowired
    private ShapeService shapeService;

    @Autowired
    private AppUserService appUserService;

    private UserDetails currentUser;

    @BeforeEach
    public void setUp() {
        currentUser = appUserService.loadUserByUsername("creator");
    }

    @Test
    public void shouldUpdateShapeMethodThrowOptimisticLockingExceptionWhileTryingToUpdateTheSameFigureAtTheSameTime() {
        //given
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("circle");
        circleCommand.setParameters(Map.of("radius", 5.0));

        Shape shape = shapeService.createShape(circleCommand);
        Long shapeId = shape.getId();

        ShapeUpdateCommand updateCommand1 = new ShapeUpdateCommand();
        updateCommand1.setParameters(Map.of("radius", 6.0));

        ShapeUpdateCommand updateCommand2 = new ShapeUpdateCommand();
        updateCommand2.setParameters(Map.of("radius", 7.0));

        //when - then
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Callable<Void> task1 = () -> {
            shapeService.updateShape(shapeId, updateCommand1, (AppUser) currentUser);
            return null;
        };

        Callable<Void> task2 = () -> {
            shapeService.updateShape(shapeId, updateCommand2, (AppUser) currentUser);
            return null;
        };

        Future<Void> future1 = executorService.submit(task1);
        Future<Void> future2 = executorService.submit(task2);

        try {
            future1.get();
            future2.get();
            fail("Expected an ObjectOptimisticLockingFailureException to be thrown");
        } catch (ExecutionException | InterruptedException e) {
            Throwable cause = e.getCause();
            System.out.println(cause);
            assertTrue(cause instanceof ObjectOptimisticLockingFailureException);
            assertTrue(cause.getClass().getSimpleName().contains("ObjectOptimisticLockingFailureException"));
        }

        executorService.shutdown();
    }

    @Test
    public void shouldUpdateTwoDifferentShapesAtTheSameTimeWithoutThrowingAnyExceptions() {
        // Given
        CreateShapeCommand circleCommand = new CreateShapeCommand();
        circleCommand.setShapeType("circle");
        circleCommand.setParameters(Map.of("radius", 5.0));

        Shape shape = shapeService.createShape(circleCommand);
        Long shapeId = shape.getId();

        Shape shape2 = shapeService.createShape(circleCommand);
        Long shapeId2 = shape2.getId();

        ShapeUpdateCommand updateCommand1 = new ShapeUpdateCommand();
        updateCommand1.setParameters(Map.of("radius", 6.0));

        ShapeUpdateCommand updateCommand2 = new ShapeUpdateCommand();
        updateCommand2.setParameters(Map.of("radius", 7.0));

        //when - then
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Callable<Void> task1 = () -> {
            shapeService.updateShape(shapeId, updateCommand1, (AppUser) currentUser);
            return null;
        };

        Callable<Void> task2 = () -> {
            shapeService.updateShape(shapeId2, updateCommand2, (AppUser) currentUser);
            return null;
        };

        Future<Void> future1 = executorService.submit(task1);
        Future<Void> future2 = executorService.submit(task2);

        try {
            future1.get();
            future2.get();
        } catch (ExecutionException | InterruptedException e) {
            fail("No exception should be thrown, but found: " + e);
        }

        executorService.shutdown();
    }
}