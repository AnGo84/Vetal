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
@Table(name = "productions")
public class ProductionDirectory extends AbstractDirectory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	public Long getId() {
		return id;
	}

	@NotNull
	@Size(max = 250)
	@Column(name = "FullName", length = 250, nullable = false, unique = true)
	public String getName() {
		return name;
	}

}
