package ua.com.vetal.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "Payments", //
        uniqueConstraints = { //
                @UniqueConstraint(name = "Payment_UK", columnNames = "name")})
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotEmpty
    @Column(name = "name", length = 30, nullable = false)
    private String name;
    @Column(name = "altname", length = 45)
    private String altName;
}
