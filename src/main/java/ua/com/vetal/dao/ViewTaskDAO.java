package ua.com.vetal.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.ViewTask;
import ua.com.vetal.entity.filter.FilterData;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ViewTaskDAO {

    @PersistenceContext
    private EntityManager entityManager;


    public List<ViewTask> findByFilterData(FilterData filterData) {
        List<ViewTask> list = null;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ViewTask> query = builder.createQuery(ViewTask.class);
        Root<ViewTask> root = query.from(ViewTask.class);

        Predicate predicate = builder.conjunction();

        if (filterData != null) {

            if (filterData.getAccount() != null && !filterData.getAccount().equals("")) {
                predicate = builder.and(predicate, builder.equal(root.get("account"), filterData.getAccount()));
                /*
                 * predicate = builder.and(predicate,
                 * builder.like(builder.lower(builder.toString(root.get("account")))
                 * , ("%" + filterData.getAccount() + "%").toLowerCase()));
                 */
                predicate = builder.and(predicate, builder.like(builder.lower(root.get("account")),
                        ("%" + filterData.getAccount() + "%").toLowerCase()));
            }


            if (filterData.getNumber() != null && !filterData.getNumber().equals("")) {
                predicate = builder.and(predicate, builder.like(builder.lower(root.get("fullNumber")),
                        ("%" + filterData.getNumber() + "%").toLowerCase()));

            }

            if (filterData.getFileName() != null && !filterData.getFileName().equals("")) {
                predicate = builder.and(predicate, builder.like(builder.lower(root.get("fileName")),
                        ("%" + filterData.getFileName() + "%").toLowerCase()));
            }

            if (filterData.getClient() != null && filterData.getClient().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("client"), filterData.getClient()));
            }

            if (filterData.getContractor() != null && filterData.getContractor().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("contractor"), filterData.getContractor()));
            }

            if (filterData.getManager() != null && filterData.getManager().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("manager"), filterData.getManager()));
            }
            if (filterData.getPaper() != null && filterData.getPaper().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("paper"), filterData.getPaper()));
            }
            if (filterData.getProduction() != null && filterData.getProduction().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("production"), filterData.getProduction()));
            }

            if (filterData.getDateBeginFrom() != null) {
                predicate = builder.and(predicate,
                        builder.greaterThanOrEqualTo(root.get("dateBegin"), filterData.getDateBeginFrom()));
            }

            if (filterData.getDateBeginTill() != null) {
                predicate = builder.and(predicate,
                        builder.lessThanOrEqualTo(root.get("dateBegin"), filterData.getDateBeginTill()));
            }

            /*
             * for (SearchCriteria param : params) { if
             * (param.getOperation().equalsIgnoreCase(">")) { predicate =
             * builder.and(predicate,
             * builder.greaterThanOrEqualTo(root.get(param.getKey()),
             * param.getValue().toString())); } else if
             * (param.getOperation().equalsIgnoreCase("<")) { predicate =
             * builder.and(predicate,
             * builder.lessThanOrEqualTo(root.get(param.getKey()),
             * param.getValue().toString())); } else if
             * (param.getOperation().equalsIgnoreCase(":")) { if
             * (r.get(param.getKey()).getJavaType() == String.class) { predicate =
             * builder.and(predicate, builder.like(root.get(param.getKey()), "%" +
             * param.getValue() + "%")); } else { predicate = builder.and(predicate,
             * builder.equal(root.get(param.getKey()), param.getValue())); } } }
             */

        }
        query.where(predicate);
        query.orderBy(builder.desc(root.get("dateBegin")));

        list = entityManager.createQuery(query).getResultList();

        // https://www.baeldung.com/rest-search-language-spring-jpa-criteria
        // http://qaru.site/questions/293915/spring-data-jpa-query-by-example
        return list;
    }

}
