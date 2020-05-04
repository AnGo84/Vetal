package ua.com.vetal.entity.filter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface ViewFilter {
    boolean hasData();

    ViewFilter getDefault();

    Predicate getPredicate(CriteriaBuilder builder, Root root, Predicate predicate);
}
