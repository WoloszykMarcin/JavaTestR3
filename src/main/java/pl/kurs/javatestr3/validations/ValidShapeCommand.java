package pl.kurs.javatestr3.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ShapeCommandValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidShapeCommand {

    String message() default "Invalid shape command provided.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
