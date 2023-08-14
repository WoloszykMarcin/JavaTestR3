package pl.kurs.javatestr3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kurs.javatestr3.repository.AppUserRepository;
import pl.kurs.javatestr3.security.AppRole;
import pl.kurs.javatestr3.security.AppUser;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        AppRole creatorRole = new AppRole("ROLE_CREATOR");
        AppRole userRole = new AppRole("ROLE_USER");

        AppUser admin = new AppUser("creator", passwordEncoder.encode("creator"), Set.of(creatorRole));
        AppUser user = new AppUser("user", passwordEncoder.encode("user"), Set.of(userRole));

        appUserRepository.saveAll(List.of(admin, user));
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return appUserRepository.findByUsernameWithRoles(s)
                .orElseThrow(() -> new UsernameNotFoundException(s));
    }
}
