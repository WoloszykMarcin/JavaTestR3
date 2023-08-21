package pl.kurs.javatestr3.security;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.kurs.javatestr3.model.inheritance.Shape;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@ToString(exclude = {"createdShapes", "modifiedShapes"})
@EqualsAndHashCode(exclude = {"createdShapes", "modifiedShapes"})
@NoArgsConstructor
public class AppUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;
    private String username;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<AppRole> roles = new HashSet<>();

    @OneToMany(mappedBy = "createdBy")
    private Set<Shape> createdShapes = new HashSet<>();

    @OneToMany(mappedBy = "lastModifiedBy")
    private Set<Shape> modifiedShapes = new HashSet<>();


    public AppUser(String username, String password, Set<AppRole> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public AppUser(String firstName, String lastName, String username, String password, Set<AppRole> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
