package pl.kurs.javatestr3.dto;

import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Value
@Builder
public class UserFullDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private Set<String> appRoles;
    private int numberOfCreatedFigures;
}

