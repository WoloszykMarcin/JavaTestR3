package pl.kurs.javatestr3.model.inheritance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private int id;

    @Column(name = "type", insertable = false, updatable = false)
    private String type;

    @Version
    private int version;

    @CreatedDate
    private LocalDateTime createdDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @LastModifiedBy
    private String lastModifiedBy;

    @Column
    private double area;

    @Column
    private double perimeter;

    public Shape(String type) {
        this.type = type;
    }

    @PrePersist
    @PreUpdate
    private void updateCalculatedFields() {
        this.area = calculateArea();
        this.perimeter = calculatePerimeter();
    }

    public abstract double calculatePerimeter();

    public abstract double calculateArea();
}
