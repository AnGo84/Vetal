package ua.com.vetal.entity;

import lombok.Data;
import ua.com.vetal.entity.pk.CompositeAppUserRolePK;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "USER_ROLE", //
        uniqueConstraints = { //
                @UniqueConstraint(name = "USER_ROLE_UK", columnNames = {"User_Id", "Role_Id"})})
@Data
public class AppUserRole implements Serializable {

    @EmbeddedId
    private CompositeAppUserRolePK appUserRoleID;

    @MapsId("appUserID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_Id", nullable = false)
    private AppUser appUser;

    @MapsId("appUserRoleID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Role_Id", nullable = false)
    private AppRole appRole;
}
