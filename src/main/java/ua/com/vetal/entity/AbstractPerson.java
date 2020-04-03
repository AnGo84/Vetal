package ua.com.vetal.entity;

import org.apache.logging.log4j.util.Strings;

public abstract class AbstractPerson {
    Long id;

    String corpName;

    String shortName;

    String lastName;

	String firstName;

	String middleName;

	String email;

	String phone;

	public abstract Long getId();

	public void setId(Long id) {
		this.id = id;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public abstract String getLastName();

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public abstract String getFirstName();

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public abstract String getMiddleName();

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFullName() {
        String result = "";
        if (!Strings.isBlank(corpName)) {
            return corpName;
        }
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AbstractPerson)) return false;

		AbstractPerson that = (AbstractPerson) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (corpName != null ? !corpName.equals(that.corpName) : that.corpName != null) return false;
		if (shortName != null ? !shortName.equals(that.shortName) : that.shortName != null) return false;
		if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
		if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
		if (middleName != null ? !middleName.equals(that.middleName) : that.middleName != null) return false;
		if (email != null ? !email.equals(that.email) : that.email != null) return false;
		return phone != null ? phone.equals(that.phone) : that.phone == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (corpName != null ? corpName.hashCode() : 0);
		result = 31 * result + (shortName != null ? shortName.hashCode() : 0);
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		result = 31 * result + (middleName != null ? middleName.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (phone != null ? phone.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("AbstractPerson{");
		sb.append("id=").append(id);
		sb.append(", corpName='").append(corpName).append('\'');
		sb.append(", shortName='").append(shortName).append('\'');
		sb.append(", lastName='").append(lastName).append('\'');
		sb.append(", firstName='").append(firstName).append('\'');
		sb.append(", middleName='").append(middleName).append('\'');
		sb.append(", email='").append(email).append('\'');
		sb.append(", phone='").append(phone).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
