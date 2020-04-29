package ua.com.vetal.entity.pk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompositeAppUserRolePK implements Serializable {
    private Long appUserID;
    private Long appUserRoleID;
}
