package ua.com.vetal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.dao.StencilDAO;
import ua.com.vetal.entity.Stencil;
import ua.com.vetal.entity.filter.OrderViewFilter;
import ua.com.vetal.repositories.StencilRepository;

import java.util.List;

@Service("stencilService")
@Transactional
public class StencilServiceImpl implements SimpleService<Stencil> {

    private static final Logger logger = LoggerFactory.getLogger(StencilServiceImpl.class);
    @Autowired
    private StencilDAO stencilDAO;
    @Autowired
    private StencilRepository stencilRepository;

    @Override
    public Stencil findById(Long id) {
        /*
         * Optional<User> optinalEntity = userRepository.findById(id); User user
         * = optinalEntity.get(); return user;
         */
        return stencilRepository.getOne(id);
    }

    @Override
    public Stencil findByName(String name) {
        // return directoryRepository.findByName(name);
        return null;
    }

    @Override
    public void saveObject(Stencil stencil) {
        stencilRepository.save(stencil);
    }

    @Override
    public void updateObject(Stencil stencil) {
        saveObject(stencil);
    }

    @Override
    public void deleteById(Long id) {
        stencilRepository.deleteById(id);
    }

    @Override
    public List<Stencil> findAllObjects() {
        // logger.info("Get all TASKS");
        List<Stencil> getList = stencilRepository.findAll(sortByDateBeginDesc());
        // List<Task> getList = stencilRepository.findAllByOrderByDateBeginDesc();
        // logger.info("List size: " + getList.size());
        return getList;
    }

    public Stencil findByAccount(String account) {
        return stencilRepository.findByAccount(account);
    }

    public List<Stencil> findByFilterData(OrderViewFilter orderViewFilter) {
        List<Stencil> list = stencilDAO.findByFilterData(orderViewFilter);

        if (orderViewFilter == null) {
            return findAllObjects();
        }

        /*if (filterData == null) {
            return findAllObjects();
        }

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Stencil> query = builder.createQuery(Stencil.class);
        Root<Stencil> root = query.from(Stencil.class);

        Predicate predicate = builder.conjunction();


        if (filterData.getAccount() != null && !filterData.getAccount().equals("")) {
//			predicate = builder.and(predicate, builder.equal(root.get("account"), filterData.getAccount()));
            predicate = builder.and(predicate, builder.like(builder.lower(root.get("account")),
                    ("%" + filterData.getAccount() + "%").toLowerCase()));

        }

        if (filterData.getNumber() != null && !filterData.getNumber().equals("")) {
            predicate = builder.and(predicate, builder.like(builder.lower(root.get("fullNumber")),
                    ("%" + filterData.getNumber() + "%").toLowerCase()));

        }

		*//*if (filterData.getFileName() != null && !filterData.getFileName().equals("")) {
			predicate = builder.and(predicate, builder.like(builder.lower(root.get("fileName")),
					("%" + filterData.getFileName() + "%").toLowerCase()));
		}*//*

        if (filterData.getClient() != null && filterData.getClient().getId() != 0) {
            predicate = builder.and(predicate, builder.equal(root.get("client"), filterData.getClient()));
        }

        if (filterData.getPrinter() != null && filterData.getPrinter().getId() != 0) {
            predicate = builder.and(predicate, builder.equal(root.get("printer"), filterData.getPrinter()));
        }

        if (filterData.getManager() != null && filterData.getManager().getId() != 0) {
            predicate = builder.and(predicate, builder.equal(root.get("manager"), filterData.getManager()));
        }

        if (filterData.getPaper() != null && filterData.getPaper().getId() != 0) {
            predicate = builder.and(predicate, builder.equal(root.get("paper"), filterData.getPaper()));
        }

        if (filterData.getProduction() != null && filterData.getProduction().getId() != 0) {
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

        query.where(predicate);
        query.orderBy(builder.desc(root.get("dateBegin")));

        list = entityManager.createQuery(query).getResultList();
*/
        // https://www.baeldung.com/rest-search-language-spring-jpa-criteria
        // http://qaru.site/questions/293915/spring-data-jpa-query-by-example
        return list;
    }

    @Override
    public boolean isObjectExist(Stencil stencil) {
        // return findByName(manager.getName()) != null;
        return findById(stencil.getId()) != null;
    }

    private Sort sortByDateBeginDesc() {
        //return new Sort(Sort.Direction.DESC, "dateBegin");
        return Sort.by(Sort.Direction.DESC, "dateBegin");
    }

    public boolean isAccountValueExist(Stencil stencil) {

        Stencil findStencil = findByAccount(stencil.getAccount());

        /*
         * System.out.println(findTask); System.out.println(findTask != null &&
         * findTask.getId()!=null && !findTask.getId().equals(task.getId()));
         * System.out.println((findTask != null )+": "+ (findTask.getId()!=null)
         * +": "+ (!findTask.getId().equals(task.getId())));
         * System.out.println(findTask.getId() +" : "+task.getId());
         */
        return (findStencil != null && findStencil.getId() != null && !findStencil.getId().equals(stencil.getId()));

        // return findByAccount(task.getAccount()) != null;
    }

    public Long getMaxID() {
        return stencilDAO.getMaxID();
    }

}
