package ua.com.vetal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.dao.ContractorDAO;
import ua.com.vetal.entity.Contractor;
import ua.com.vetal.entity.filter.PersonFilter;
import ua.com.vetal.repositories.ContractorRepository;

import java.util.List;

@Service("contractorService")
@Transactional
public class ContractorServiceImpl implements SimpleService<Contractor> {

	// private static final Logger logger =
	// LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private ContractorDAO contractorDAO;

	@Autowired
	private ContractorRepository personRepository;
	/*@PersistenceContext
	private EntityManager entityManager;*/

	@Override
	public Contractor findById(Long id) {
		/*
		 * Optional<User> optinalEntity = userRepository.findById(id); User user
		 * = optinalEntity.get(); return user;
		 */
		return personRepository.getOne(id);
	}

	@Override
	public Contractor findByName(String name) {
		return personRepository.findByCorpName(name);
	}

	public Contractor findByEmail(String email) {
		return personRepository.findByEmail(email);
	}

	@Override
	public void saveObject(Contractor person) {
		personRepository.save(person);
	}

	@Override
	public void updateObject(Contractor person) {
		saveObject(person);
	}

	@Override
	public void deleteById(Long id) {
		personRepository.deleteById(id);
	}

	@Override
	public List<Contractor> findAllObjects() {
		return personRepository.findAll();
	}

	@Override
	public boolean isObjectExist(Contractor person) {
		return findById(person.getId()) != null;
	}

	public boolean isCorpNameExist(Contractor person) {
		return findByName(person.getCorpName()) != null;
	}

	public List<Contractor> findByFilterData(PersonFilter filterData) {
		List<Contractor> list = contractorDAO.findByFilterData(filterData);

		if (filterData == null) {
			return findAllObjects();
		}

		/*CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Contractor> query = builder.createQuery(Contractor.class);
		Root<Contractor> root = query.from(Contractor.class);

		Predicate predicate = builder.conjunction();


		if (!Strings.isBlank(filterData.getCorpName())) {
//			predicate = builder.and(predicate, builder.equal(root.get("account"), filterData.getAccount()));
			predicate = builder.and(predicate, builder.like(builder.lower(root.get("corpName")),
					("%" + filterData.getCorpName() + "%").toLowerCase()));

		}

		if (filterData.getManager() != null && filterData.getManager().getId() != 0) {
			predicate = builder.and(predicate, builder.equal(root.get("manager"), filterData.getManager()));
		}

		query.where(predicate);
		//query.orderBy(builder.desc(root.get("dateBegin")));

		list = entityManager.createQuery(query).getResultList();*/

		// https://www.baeldung.com/rest-search-language-spring-jpa-criteria
		// http://qaru.site/questions/293915/spring-data-jpa-query-by-example
		return list;
	}

}
