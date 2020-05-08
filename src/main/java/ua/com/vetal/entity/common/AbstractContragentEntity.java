package ua.com.vetal.entity.common;

import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import ua.com.vetal.entity.Manager;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@MappedSuperclass
@Data
public abstract class AbstractContragentEntity extends AbstractEntity {
    @NotEmpty
    @Size(min = 0, max = 250)
    @Column(name = "CorpName", length = 250, nullable = false)
    public String corpName;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @NotEmpty
    @Size(min = 0, max = 50)
    @Column(name = "LastName", length = 50, nullable = false)
    private String lastName;

    @NotEmpty
    @Size(min = 0, max = 50)
    @Column(name = "FirstName", length = 50, nullable = false)
    private String firstName;

    @Size(max = 50)
    @Column(name = "MiddleName", length = 50, nullable = true)
    private String middleName;

    @NotEmpty
    @Size(min = 0, max = 250)
    @Column(name = "email", length = 250, nullable = true)
    private String email;

    @NotEmpty
    @Size(max = 250)
    @Column(name = "phone", length = 250, nullable = false)
    private String phone;

    @NotEmpty
    @Size(max = 250)
    @Column(name = "address", length = 250, nullable = false)
    private String address;

    @Size(max = 100)
    @Column(name = "site_url", length = 100)
    private String siteURL;

    public String getFullName() {
        if (!Strings.isBlank(corpName)) {
            return corpName;
        }

        return getPersonFullName();
    }

    public String getPersonFullName() {
        String result = "";
        if (!Strings.isBlank(lastName)) {
            result += (result.equals("") ? "" : " ") + lastName;
        }
        if (!Strings.isBlank(firstName)) {
            result += (result.equals("") ? "" : " ") + firstName;
        }
        if (!Strings.isBlank(middleName)) {
            result += (result.equals("") ? "" : " ") + middleName;
        }
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AbstractContragentEntity{");
        sb.append("id='").append(getId()).append('\'');
        sb.append(", corpName='").append(corpName).append('\'');
        sb.append(", manager=").append(manager);
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", middleName='").append(middleName).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", siteURL='").append(siteURL).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
