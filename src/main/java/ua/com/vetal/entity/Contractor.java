package ua.com.vetal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "contractors")
public class Contractor extends AbstractPerson {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	public Long getId() {
		return id;
	}

	@NotNull
	@Size(min = 0, max = 250)
	@Column(name = "CorpName", length = 250, nullable = false)
	public String getCorpName() {
		return corpName;
	}


	@Size(min = 0, max = 50)
	@Column(name = "LastName", length = 50, nullable = false)
	public String getLastName() {
		return lastName;
	}

	@Size(min = 0, max = 50)
	@Column(name = "FirstName", length = 50, nullable = true)
	public String getFirstName() {
		return firstName;
	}

	@Size(min = 0, max = 50)
	@Column(name = "MiddleName", length = 50, nullable = true)
	public String getMiddleName() {
		return middleName;
	}

	@Size(min = 0, max = 100)
	@Column(name = "email", length = 50, nullable = true)
	public String getEmail() {
		return email;
	}

}
