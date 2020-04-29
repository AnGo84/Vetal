package ua.com.vetal.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "Payments", //
        uniqueConstraints = { //
                @UniqueConstraint(name = "Payment_UK", columnNames = "name")})
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotEmpty
    @Column(name = "name", length = 30, nullable = false)
    private String name;
    @Column(name = "altname", length = 45)
    private String altName;

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

    public String getAltName() {
        return altName;
    }

    public void setAltName(String altName) {
        this.altName = altName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment)) return false;

        Payment state = (Payment) o;

        if (id != null ? !id.equals(state.id) : state.id != null) return false;
        if (name != null ? !name.equals(state.name) : state.name != null) return false;
        return altName != null ? altName.equals(state.altName) : state.altName == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (altName != null ? altName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Payment{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", altName='").append(altName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
