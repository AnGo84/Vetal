package ua.com.vetal.entity.common;

import lombok.EqualsAndHashCode;
import org.apache.logging.log4j.util.Strings;
import ua.com.vetal.utils.StringUtils;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.StringJoiner;

@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractEmployeeEntity extends AbstractEntity {

    private String lastName;

    private String firstName;

    private String middleName;

    private String email;

    private String phone;

    private String address;

    @NotEmpty
    @Size(max = 50)
    @Column(name = "LastName", length = 50, nullable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Size(max = 50)
    @Column(name = "FirstName", length = 50, nullable = true)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Size(max = 50)
    @Column(name = "MiddleName", length = 50, nullable = true)
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Size(max = 100)
    @Column(name = "Email", length = 100, nullable = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Transient
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Transient
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Transient
    public String getFullName() {
        StringJoiner joiner = new StringJoiner(" ");

        if (!Strings.isBlank(lastName)) {
            joiner.add(lastName);
        }
        if (!Strings.isBlank(firstName)) {
            joiner.add(firstName);
        }
        if (!Strings.isBlank(middleName)) {
            joiner.add(middleName);
        }
        String mergedName = joiner.toString();
        return StringUtils.isEmpty(mergedName) ? "" : mergedName;
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
