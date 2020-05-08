package ua.com.vetal.entity.common;

import lombok.Data;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@MappedSuperclass
@Data
public abstract class AbstractEmployeeEntity extends AbstractEntity {
    @NotEmpty
    @Size(max = 50)
    @Column(name = "LastName", length = 50, nullable = false)
    private String lastName;

    @Size(max = 50)
    @Column(name = "FirstName", length = 50, nullable = true)
    private String firstName;

    @Size(max = 50)
    @Column(name = "MiddleName", length = 50, nullable = true)
    private String middleName;

    @Size(max = 100)
    @Column(name = "Email", length = 100, nullable = true)
    private String email;

    private String phone;

    private String address;

    public String getFullName() {
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
        final StringBuffer sb = new StringBuffer("AbstractEmployeeEntity{");
        sb.append("id='").append(getId()).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", middleName='").append(middleName).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
