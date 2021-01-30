package ua.com.vetal.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "links")
@Data
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotEmpty
    @Column(name = "FullName", length = 250, nullable = false)
    private String fullName;

    @Column(name = "ShortName", length = 50, nullable = true)
    private String shortName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Link_Type_ID")
    private LinkType linkType;

    @NotEmpty
    @Column(name = "path", length = 500, nullable = false)
    private String path;

}
