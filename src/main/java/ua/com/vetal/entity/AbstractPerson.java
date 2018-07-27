package ua.com.vetal.entity;

public abstract class AbstractPerson {
	Long id;

	String lastName;

	String firstName;

	String middleName;

	public abstract Long getId();

	public void setId(Long id) {
		this.id = id;
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

	public String getFullName() {
		String result = "";
		if (lastName != null) {
			result += (result.equals("") ? "" : " ") + lastName;
		}
		if (firstName != null) {
			result += (result.equals("") ? "" : " ") + firstName;
		}
		if (middleName != null) {
			result += (result.equals("") ? "" : " ") + middleName;
		}
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((middleName == null) ? 0 : middleName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractPerson other = (AbstractPerson) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (middleName == null) {
			if (other.middleName != null)
				return false;
		} else if (!middleName.equals(other.middleName))
			return false;
		return true;
	}

}
