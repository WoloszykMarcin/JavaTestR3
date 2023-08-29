package pl.kurs.javatestr3.model.inheritance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.kurs.javatestr3.security.AppUser;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class Shape {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", insertable = false, updatable = false)
    private String type;

    @Version
    private int version;

    @CreatedDate
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    @CreatedBy
    private AppUser createdBy;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @ManyToOne
    @JoinColumn(name = "last_modified_by_id")
    @LastModifiedBy
    private AppUser lastModifiedBy;

    public Shape(String type) {
        this.type = type;
    }

    public double getCalculatedArea() {
        return getArea();
    }

    public double getCalculatedPerimeter() {
        return getPerimeter();
    }

    public abstract Map<String, Object> getParameters();

    public abstract double getArea();

    public abstract double getPerimeter();
    public abstract void setField(String fieldName, Object value);

}
