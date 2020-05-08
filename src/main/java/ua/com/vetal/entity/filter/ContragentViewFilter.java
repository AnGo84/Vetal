package ua.com.vetal.entity.filter;

import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import ua.com.vetal.entity.Manager;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Data
public class ContragentViewFilter implements ViewFilter {
    private String corpName;
    private Manager manager;

    @Override
    public boolean hasData() {
        if (!Strings.isBlank(corpName)) {
            return true;
        } else if (manager != null) {
            return true;
        }
        return false;
    }

    @Override
    public ViewFilter getDefault() {
        return new ContragentViewFilter();
    }

    @Override
    public Predicate getPredicate(CriteriaBuilder builder, Root root) {
        Predicate predicate = builder.conjunction();
        if (this == null || !this.hasData()) {
            return predicate;
        }
        if (!Strings.isBlank(this.getCorpName())) {
            predicate = builder.and(predicate, builder.like(builder.lower(root.get("corpName")),
                    ("%" + this.getCorpName() + "%").toLowerCase()));
        }
        if (this.getManager() != null && this.getManager().getId() != null) {
            predicate = builder.and(predicate, builder.equal(root.get("manager"), this.getManager()));
        }
        return predicate;
    }
}
