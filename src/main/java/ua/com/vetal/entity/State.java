package ua.com.vetal.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

/**
 * 1 - New
 * 2 - In_work
 * 3 - Printed
 * 4 - Ready
 * 5 - Shiped
 */

@Entity
@Table(name = "States", //
		uniqueConstraints = { //
				@UniqueConstraint(name = "States_UK", columnNames = "name")})
@Data
public class State {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@NotEmpty
	@Column(name = "name", length = 30, nullable = false)
	private String name;
	@Column(name = "altname", length = 45)
	private String altName;

}
