package pl.kurs.javatestr3.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pl.kurs.javatestr3.repository.AppUserRepository;

import java.util.Optional;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<AppUser> {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public Optional<AppUser> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            return Optional.empty();

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        return appUserRepository.findByUsernameWithRoles(username);
    }
}
