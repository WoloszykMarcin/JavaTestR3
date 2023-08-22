package pl.kurs.javatestr3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.javatestr3.security.AppUser;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Query("SELECT u FROM AppUser u LEFT JOIN FETCH u.roles WHERE u.username = ?1")
    Optional<AppUser> findByUsernameWithRoles(String username);

    @Query("SELECT COUNT(s) FROM AppUser u JOIN u.createdShapes s WHERE u.id = :id")
    int countCreatedShapesByUserId(@Param("id") Long id);

    @Query(value = "SELECT DISTINCT u FROM AppUser u LEFT JOIN FETCH u.roles",
            countQuery = "SELECT COUNT(u) FROM AppUser u")
    Page<AppUser> findAllWithRoles(Pageable pageable);

    boolean existsByUsername(String username);
}
