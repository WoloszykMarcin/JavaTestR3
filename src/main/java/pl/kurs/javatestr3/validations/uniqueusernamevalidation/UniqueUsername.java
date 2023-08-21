package pl.kurs.javatestr3.validations.uniqueusernamevalidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) //mowi o tym w którym miejscu możemy stawiać adnotację
@Retention(RetentionPolicy.RUNTIME) //w jakiej fazie kodu adnotacja bedzie procesowana (np. Runtime - w trakcie działania aplikacji)
@Constraint(validatedBy = UniqueUsernameValidator.class)
public @interface UniqueUsername {
    String message() default "USERNAME_NOT_UNIQUE";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

