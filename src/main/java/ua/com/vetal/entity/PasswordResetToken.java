package ua.com.vetal.entity;

import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken {
	//private static final int EXPIRATION = 60 * 24; //24 hours

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String token;

	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;

	@Column(name = "expiry_date", nullable = false)
	//@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date expiryDate;

	public static Builder newBuilder() {
		return new PasswordResetToken().new Builder();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public void setExpiryDate(int minutes) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MINUTE, minutes);
		this.expiryDate = now.getTime();
	}

	public boolean isExpired() {
		return new Date().after(this.expiryDate);
	}

	public class Builder {

		private Builder() {
			// private constructor
		}

		public Builder setUser(User user) {
			PasswordResetToken.this.user = user;
			return this;
		}

		public Builder setToken(String token) {
			PasswordResetToken.this.token = token;
			return this;
		}

		public Builder setToken(Date expiryDate) {
			PasswordResetToken.this.expiryDate = expiryDate;
			return this;
		}

		public PasswordResetToken build() {
			//PasswordResetToken token = new PasswordResetToken();
			if (StringUtils.isBlank(PasswordResetToken.this.token)) {
				PasswordResetToken.this.token = UUID.randomUUID().toString();
			}
			if (PasswordResetToken.this.expiryDate == null) {
				PasswordResetToken.this.setExpiryDate(60); // 1 hour
			}
			return PasswordResetToken.this;
		}
	}
}
