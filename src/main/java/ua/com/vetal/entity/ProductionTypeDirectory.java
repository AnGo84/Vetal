package ua.com.vetal.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "production_types")
public class ProductionTypeDirectory extends AbstractDirectory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    @NotNull
    @Size(max = 250)
    @Column(name = "FullName", length = 250, nullable = false, unique = true)
    public String getName() {
        return name;
    }

}
