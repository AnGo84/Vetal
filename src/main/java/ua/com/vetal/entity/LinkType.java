package ua.com.vetal.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 1-link, 2- file, 3-folder
 */
@Entity
@Table(name = "link_type", //
        uniqueConstraints = { //
                @UniqueConstraint(name = "LINK_TYPE_UK", columnNames = "Type_name")})
@Data
public class LinkType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotEmpty
    @Size(min = 1, max = 30)
    @Column(name = "Type_name", length = 30, nullable = false)
    private String name;
}
