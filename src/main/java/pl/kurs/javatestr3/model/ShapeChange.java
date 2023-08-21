package pl.kurs.javatestr3.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.kurs.javatestr3.model.inheritance.Shape;
import pl.kurs.javatestr3.security.AppUser;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ShapeChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Shape shape;

    private String modifiedField;

    private String oldValue;

    private String newValue;

    private LocalDateTime changeDate;

    @ManyToOne
    private AppUser modifiedBy;

}
