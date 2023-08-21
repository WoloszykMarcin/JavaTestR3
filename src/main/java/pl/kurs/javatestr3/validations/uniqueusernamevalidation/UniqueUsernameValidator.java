package pl.kurs.javatestr3.validations.uniqueusernamevalidation;

import org.springframework.stereotype.Service;
import pl.kurs.javatestr3.repository.AppUserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Service
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final AppUserRepository appUserRepository;

    public UniqueUsernameValidator(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return !appUserRepository.existsByUsername(username);
    }
}
