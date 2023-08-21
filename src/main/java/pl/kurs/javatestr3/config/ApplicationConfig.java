package pl.kurs.javatestr3.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import pl.kurs.javatestr3.security.AppUser;
import pl.kurs.javatestr3.security.SpringSecurityAuditorAware;

import java.util.Optional;
import java.util.Set;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "appUserAuditorProvider")
public class ApplicationConfig {

    @Bean
    public ModelMapper modelMapper(Set<Converter> converters) {
        ModelMapper modelMapper = new ModelMapper();

        //mapping configuration for retrieving username from AppUser
        modelMapper.createTypeMap(AppUser.class, String.class).setConverter(context -> context.getSource().getUsername());

        converters.forEach(modelMapper::addConverter);
        return modelMapper;
    }

    @Bean(name = "usernameAuditorProvider")
    public AuditorAware<String> usernameAuditorProvider() {
        return () -> Optional.of(((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    }

    @Bean(name = "appUserAuditorProvider")
    public AuditorAware<AppUser> auditorProvider() {
        return new SpringSecurityAuditorAware();
    }
}