package pl.kurs.javatestr3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.javatestr3.security.AppRole;
import pl.kurs.javatestr3.security.AppUser;

import java.util.Optional;
import java.util.Set;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Query("SELECT u FROM AppUser u LEFT JOIN FETCH u.roles WHERE u.username = ?1")
    Optional<AppUser> findByUsernameWithRoles(String username);
    @Query(value = "SELECT u FROM AppUser u",
            countQuery = "SELECT COUNT(u) FROM AppUser u")
    Page<AppUser> findAllUsers(Pageable pageable);

    @Query("SELECT r FROM AppUser u JOIN u.roles r WHERE u.id = :userId")
    Set<AppRole> findRolesByUserId(@Param("userId") Long userId);

    boolean existsByUsername(String username);
}
