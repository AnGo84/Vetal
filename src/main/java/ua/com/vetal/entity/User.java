package ua.com.vetal.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
//@Table(name = "APP_USER", uniqueConstraints = { //
//		@UniqueConstraint(name = "APP_USER_UK", columnNames = "User_Name") })
@Table(name = "APP_USER", uniqueConstraints = { //
        @UniqueConstraint(name = "APP_USER_UK", columnNames = "User_Name")})

//uniqueConstraints = {
//@UniqueConstraint(columnNames = {"username"}),
//@UniqueConstraint(columnNames = {"email"}


public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_Id", nullable = false)
    private Long id;

    @NotEmpty
    @Size(min = 4, max = 36)
    @Column(name = "User_Name", length = 36, nullable = false, unique = true)
    private String name;

    @NotEmpty
    @Size(max = 128)
    @Column(name = "Encrypted_Password", length = 128, nullable = false)
    private String encryptedPassword;

    @NotNull
    @Column(name = "Enabled", length = 1, nullable = false)
    private boolean enabled;

    @NotEmpty
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "USER_ROLE",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName="User_Id")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName="ROLE_ID")})
    private Set<UserRole> userRoles = new HashSet<>();

    @Size(max = 100)
    @Column(name = "Email", length = 100)
    private String email;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", enabled=").append(enabled);
        sb.append(", userRoles=").append(userRoles);
        sb.append(", email=").append(email);
        sb.append('}');
        return sb.toString();
    }
}
