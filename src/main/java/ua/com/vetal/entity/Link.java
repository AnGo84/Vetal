package ua.com.vetal.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "links")
public class Link {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@NotNull
	@Column(name = "FullName", length = 250, nullable = false)
	private String fullName;

	@Column(name = "ShortName", length = 50, nullable = true)
	private String shortName;

	@ManyToOne(optional = false)
	@JoinColumn(name = "Link_Type_ID")
	private LinkType linkType;

	@NotNull
	@Column(name = "path", length = 500, nullable = false)
	private String path;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public LinkType getLinkType() {
		return linkType;
	}

	public void setLinkType(LinkType linkType) {
		this.linkType = linkType;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Link{");
		sb.append("id=").append(id);
		sb.append(", fullName='").append(fullName).append('\'');
		sb.append(", shortName='").append(shortName).append('\'');
		sb.append(", linkType=").append(linkType);
		sb.append(", path='").append(path).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
