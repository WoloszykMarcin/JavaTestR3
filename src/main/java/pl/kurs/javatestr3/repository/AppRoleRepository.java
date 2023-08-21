package pl.kurs.javatestr3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.javatestr3.security.AppRole;

import java.util.Optional;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
    Optional<AppRole> findByName(String name);
}
