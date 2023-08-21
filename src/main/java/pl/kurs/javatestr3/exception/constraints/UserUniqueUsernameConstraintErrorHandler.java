package pl.kurs.javatestr3.exception.constraints;


import org.springframework.stereotype.Service;
import pl.kurs.javatestr3.exception.handler.GlobalExceptionHandler;

@Service
public class UserUniqueUsernameConstraintErrorHandler implements ConstraintErrorHandler {
    @Override
    public GlobalExceptionHandler.ValidationErrorDto mapToErrorDto() {
        return new GlobalExceptionHandler.ValidationErrorDto("USERNAME_NOT_UNIQUE", "username");
    }

    @Override
    public String getConstraintName() {
        return "UC_USER_USERNAME";
    }
}
