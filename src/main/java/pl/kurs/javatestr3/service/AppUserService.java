package pl.kurs.javatestr3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.javatestr3.exception.customexceptions.InvalidRoleException;
import pl.kurs.javatestr3.repository.AppRoleRepository;
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
    private final AppRoleRepository appRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        AppRole creatorRole = new AppRole("ROLE_CREATOR");
        AppRole adminRole = new AppRole("ROLE_ADMIN");
        AppRole userRole = new AppRole("ROLE_USER");

        AppUser admin = new AppUser("Optimus", "Prime", "creator", passwordEncoder.encode("creator"), Set.of(creatorRole));
        AppUser user = new AppUser("Anna", "Kowalska", "user", passwordEncoder.encode("user"), Set.of(userRole));
        AppUser adminForTests = new AppUser("admin", "admin", "admin", passwordEncoder.encode("admin"), Set.of(adminRole));

        appUserRepository.saveAll(List.of(admin, user, adminForTests));
    }

    @Transactional
    public AppUser createUser(AppUser user, String roleName) {
        final String finalRoleName = (roleName == null || roleName.isEmpty()) ? "ROLE_CREATOR" : roleName;

        AppRole userRole = appRoleRepository.findByName(finalRoleName)
                .orElseThrow(() -> new InvalidRoleException("Role " + finalRoleName + " does not exist"));

        user.setRoles(Set.of(userRole));
        return appUserRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Page<AppUser> findAllUsers(Pageable pageable) {
        Page<Long> idsPage = appUserRepository.findIds(pageable);
        List<AppUser> users = appUserRepository.findAllWithRolesAndShapesByIds(idsPage.getContent());

        return new PageImpl<>(users, pageable, idsPage.getTotalElements());
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return appUserRepository.findByUsernameWithRoles(s)
                .orElseThrow(() -> new UsernameNotFoundException(s));
    }
}
