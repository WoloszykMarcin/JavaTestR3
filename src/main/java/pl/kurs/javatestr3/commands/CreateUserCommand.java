package pl.kurs.javatestr3.commands;

import lombok.Getter;
import pl.kurs.javatestr3.validations.uniqueusernamevalidation.UniqueUsername;

import javax.validation.constraints.NotEmpty;

@Getter
public class CreateUserCommand {
    @NotEmpty(message = "FIRSTNAME_NOT_EMPTY")
    private String firstName;
    @NotEmpty(message = "LASTNAME_NOT_EMPTY")
    private String lastName;
    @UniqueUsername
    private String username;
    @NotEmpty(message = "PASSWORD_NOT_EMPTY")
    private String password;
    private String roleName;
}
