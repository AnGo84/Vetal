package ua.com.vetal.entity;

import ua.com.vetal.entity.common.AbstractDirectoryEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "bases")
public class NumberBaseDirectory extends AbstractDirectoryEntity {

}

/*public class NumberBaseDirectory extends AbstractDirectory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    @NotEmpty
    @Size(max = 250)
    @Column(name = "FullName", length = 250, nullable = false, unique = true)
    public String getName() {
        return name;
    }

}*/
