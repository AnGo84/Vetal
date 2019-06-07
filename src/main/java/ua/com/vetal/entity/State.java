package ua.com.vetal.entity;

import javax.persistence.*;

@Entity
@Table(name = "States", //
        uniqueConstraints = { //
                @UniqueConstraint(name = "States_UK", columnNames = "name")})
public class State {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

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
        if (!(o instanceof State)) return false;

        State state = (State) o;

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
        final StringBuilder sb = new StringBuilder("State{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", altName='").append(altName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
