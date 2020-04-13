package ua.com.vetal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.dao.ClientDAO;
import ua.com.vetal.entity.Client;
import ua.com.vetal.entity.filter.ClientFilter;
import ua.com.vetal.repositories.ClientRepository;

import java.util.List;

@Service("clientService")
@Transactional
public class ClientServiceImpl implements SimpleService<Client> {

    // private static final Logger logger =
    // LoggerFactory.getLogger(UserServiceImpl.class);

    /*@PersistenceContext
    private EntityManager entityManager;*/
    @Autowired
    private ClientDAO clientDAO;

    @Autowired
    private ClientRepository directoryRepository;

    @Override
    public Client findById(Long id) {
        /*
         * Optional<User> optinalEntity = userRepository.findById(id); User user
         * = optinalEntity.get(); return user;
         */
        return directoryRepository.getOne(id);
    }

    @Override
    public Client findByName(String name) {
        return directoryRepository.findByFullName(name);
    }

    @Override
    public void saveObject(Client directory) {
        directoryRepository.save(directory);
    }

    @Override
    public void updateObject(Client directory) {
        saveObject(directory);
    }

    @Override
    public void deleteById(Long id) {
        directoryRepository.deleteById(id);
    }

    @Override
    public List<Client> findAllObjects() {
        return directoryRepository.findAll();
    }

    @Override
    public boolean isObjectExist(Client directory) {
        return findById(directory.getId()) != null;
    }
    public boolean isFullNameExist(Client directory) {
        return findByName(directory.getFullName()) != null;
    }

    public List<Client> findByFilterData(ClientFilter filterData) {
        //https://www.baeldung.com/rest-api-search-language-spring-data-specifications
        List<Client> list = clientDAO.findByFilterData(filterData);
        if (list == null) {
            return findAllObjects();
        }
/*
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Client> query = builder.createQuery(Client.class);
        Root<Client> root = query.from(Client.class);

        Predicate predicate = builder.conjunction();

        if (!Strings.isBlank(filterData.getFullName())) {
//			predicate = builder.and(predicate, builder.equal(root.get("account"), filterData.getAccount()));
            predicate = builder.and(predicate, builder.like(builder.lower(root.get("fullName")),
                    ("%" + filterData.getFullName() + "%").toLowerCase()));
        }

        if (filterData.getManager() != null && filterData.getManager().getId() != 0) {
            predicate = builder.and(predicate, builder.equal(root.get("manager"), filterData.getManager()));
        }

        query.where(predicate);
        //query.orderBy(builder.desc(root.get("fullName")));
        list = entityManager.createQuery(query).getResultList();*/

        // https://www.baeldung.com/rest-search-language-spring-jpa-criteria
        // http://qaru.site/questions/293915/spring-data-jpa-query-by-example
        return list;
    }

}
