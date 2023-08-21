package pl.kurs.javatestr3.mappings;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.javatestr3.dto.UserFullDto;
import pl.kurs.javatestr3.repository.AppUserRepository;
import pl.kurs.javatestr3.security.AppUser;

@Service
public class UserToUserFullDtoConverter implements Converter<AppUser, UserFullDto> {

    private final AppUserRepository appUserRepository;

    public UserToUserFullDtoConverter(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserFullDto convert(MappingContext<AppUser, UserFullDto> mappingContext) {
        AppUser source = mappingContext.getSource();
        UserFullDto dto = UserFullDto.builder()
                .id(source.getId())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .username(source.getUsername())
                .appRoles(source.getRoles())
                .numberOfCreatedFigures(appUserRepository.countCreatedShapesByUserId(source.getId()))
                .build();

        return dto;
    }
}