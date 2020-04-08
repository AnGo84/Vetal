package ua.com.vetal.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "workers")
public class Worker extends AbstractPerson {

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
