package ua.com.vetal.entity.common;

import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.utils.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.StringJoiner;

@MappedSuperclass
@Data
public abstract class AbstractContragentEntity extends AbstractEntity {

	public String corpName;

	private Manager manager;

	private String lastName;

	private String firstName;

	private String middleName;

	private String email;

	private String phone;

	private String address;

	private String siteURL;

	@NotEmpty
	@Size(min = 0, max = 250)
	@Column(name = "CorpName", length = 250, nullable = false)
	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	@ManyToOne
	@JoinColumn(name = "manager_id")
	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

	@NotEmpty
	@Size(min = 0, max = 50)
	@Column(name = "LastName", length = 50, nullable = false)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@NotEmpty
	@Size(min = 0, max = 50)
	@Column(name = "FirstName", length = 50, nullable = false)
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

	@NotEmpty
	@Size(min = 0, max = 250)
	@Column(name = "email", length = 250, nullable = true)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@NotEmpty
	@Size(max = 250)
	@Column(name = "phone", length = 250, nullable = false)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@NotEmpty
	@Size(max = 250)
	@Column(name = "address", length = 250, nullable = false)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Size(max = 100)
	@Column(name = "site_url", length = 100)
	public String getSiteURL() {
		return siteURL;
	}

	public void setSiteURL(String siteURL) {
		this.siteURL = siteURL;
	}

	@Transient
	public String getFullName() {
		if (!Strings.isBlank(corpName)) {
			return corpName;
		}

		return getPersonFullName();
	}

	@Transient
	public String getPersonFullName() {

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
