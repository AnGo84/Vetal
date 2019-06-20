package ua.com.vetal.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "contractors")
public class Contractor extends AbstractPerson {


    private Manager manager;

    private String siteURL;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    //@NotEmpty
    @Size(max =100)
    @Column(name = "site_url", length = 100)
    public String getSiteURL() {
        return siteURL;
    }

    public void setSiteURL(String siteURL) {
        this.siteURL = siteURL;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    @NotEmpty
    @Size(min = 0, max = 250)
    @Column(name = "CorpName", length = 250, nullable = false)
    public String getCorpName() {
        return corpName;
    }

    @NotEmpty
    @Size(min = 0, max = 50)
    @Column(name = "LastName", length = 50, nullable = false)
    public String getLastName() {
        return lastName;
    }

    @NotEmpty
    @Size(min = 0, max = 50)
    @Column(name = "FirstName", length = 50, nullable = false)
    public String getFirstName() {
        return firstName;
    }

    @Size(min = 0, max = 50)
    @Column(name = "MiddleName", length = 50, nullable = true)
    public String getMiddleName() {
        return middleName;
    }

    @NotEmpty
    @Size(min = 0, max = 250)
    @Column(name = "email", length = 250, nullable = true)
    public String getEmail() {
        return email;
    }

    @NotEmpty
    @Size(max = 250)
    @Column(name = "phone", length = 250, nullable = false)
    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Contractor{");

        sb.append("id=").append(id);
        sb.append(", corpName='").append(corpName).append('\'');
        sb.append(", shortName='").append(shortName).append('\'');
        sb.append(", manager=").append(manager);
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", middleName='").append(middleName).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", siteURL='").append(siteURL).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
