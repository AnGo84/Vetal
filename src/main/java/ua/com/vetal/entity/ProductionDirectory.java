package ua.com.vetal.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "productions")
public class ProductionDirectory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotEmpty
    @Column(name = "FullName", length = 250, nullable = false)
    private String fullName;

    @Column(name = "ShortName", length = 50, nullable = true)
    private String shortName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Production_Type_ID")
    //@Transient
    private ProductionTypeDirectory productionType;

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

    public ProductionTypeDirectory getProductionType() {
        return productionType;
    }

    public void setProductionType(ProductionTypeDirectory productionType) {
        this.productionType = productionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductionDirectory)) return false;

        ProductionDirectory that = (ProductionDirectory) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (fullName != null ? !fullName.equals(that.fullName) : that.fullName != null) return false;
        if (shortName != null ? !shortName.equals(that.shortName) : that.shortName != null) return false;
        return productionType != null ? productionType.equals(that.productionType) : that.productionType == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (shortName != null ? shortName.hashCode() : 0);
        result = 31 * result + (productionType != null ? productionType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProductionDirectory{");
        sb.append("id=").append(id);
        sb.append(", fullName='").append(fullName).append('\'');
        sb.append(", shortName='").append(shortName).append('\'');
        sb.append(", productionType=").append(productionType);
        sb.append('}');
        return sb.toString();
    }
}
