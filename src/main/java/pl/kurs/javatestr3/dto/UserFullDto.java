package pl.kurs.javatestr3.dto;

import lombok.Builder;
import lombok.Value;
import pl.kurs.javatestr3.security.AppRole;

import java.util.Set;

@Value
@Builder
public class UserFullDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private Set<AppRole> appRoles;
    private int numberOfCreatedFigures;
}

