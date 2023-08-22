package pl.kurs.javatestr3.commands;

import lombok.Getter;
import lombok.Setter;
import pl.kurs.javatestr3.validations.uniqueusernamevalidation.UniqueUsername;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CreateUserCommand {
    @NotEmpty(message = "FIRSTNAME_IS_EMPTY")
    private String firstName;
    @NotEmpty(message = "LASTNAME_IS_EMPTY")
    private String lastName;
    @UniqueUsername
    private String username;
    @NotEmpty(message = "PASSWORD_IS_EMPTY")
    private String password;
    private String roleName;
}
