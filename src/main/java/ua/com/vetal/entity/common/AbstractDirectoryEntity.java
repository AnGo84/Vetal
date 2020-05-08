package ua.com.vetal.entity.common;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@MappedSuperclass
@Data
public abstract class AbstractDirectoryEntity extends AbstractEntity {
	/*@NotEmpty
	@Size(max = 250)
	@Column(name = "FullName", length = 250, nullable = false, unique = true)*/
	private String name;

	@NotEmpty
	@Size(max = 250)
	@Column(name = "FullName", length = 250, nullable = false, unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer(this.getClass().getName() + "{");
		sb.append("id='").append(getId()).append('\'');
		sb.append(", name='").append(name).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
