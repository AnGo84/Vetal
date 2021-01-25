package ua.com.vetal.entity;

import ua.com.vetal.entity.common.AbstractEmployeeEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "printers")
public class Printer extends AbstractEmployeeEntity {

}
/*
public class Printer extends AbstractPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    @NotEmpty
    @Size(max = 50)
    @Column(name = "LastName", length = 50, nullable = false)
    public String getLastName() {
        return lastName;
    }

    @Size(max = 50)
    @Column(name = "FirstName", length = 50, nullable = true)
    public String getFirstName() {
        return firstName;
    }

    @Size(max = 50)
    @Column(name = "MiddleName", length = 50, nullable = true)
    public String getMiddleName() {
        return middleName;
    }

    @Size(max = 100)
    @Column(name = "Email", length = 100, nullable = true)
    public String getEmail() {
        return email;
    }

}
*/
