package ua.com.vetal.entity.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.vetal.TestDataUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractContragentEntityTest {
	private AbstractContragentEntity contragentEntity;

	@BeforeEach
	public void beforeEach() {
		contragentEntity = new TestContragent();
		contragentEntity.setId(1l);
		contragentEntity.setCorpName("CorpName");
		contragentEntity.setFirstName("FirstName");
		contragentEntity.setMiddleName("MiddleName");
		contragentEntity.setLastName("LastName");
		contragentEntity.setAddress("Address");
		contragentEntity.setEmail("Email");
		contragentEntity.setPhone("Phone");
		contragentEntity.setSiteURL("SiteURL");
		contragentEntity.setManager(TestDataUtils.getManager(1l));
	}


	@Test
	public void whenGetFullName() {
		assertEquals(contragentEntity.getCorpName(), contragentEntity.getFullName());
		contragentEntity.setCorpName(null);
		assertEquals("LastName FirstName MiddleName", contragentEntity.getFullName());
		contragentEntity.setCorpName("");
		assertEquals("LastName FirstName MiddleName", contragentEntity.getFullName());
	}

	@Test
	public void whenGetPersonFullName() {
		assertEquals("LastName FirstName MiddleName", contragentEntity.getPersonFullName());
		contragentEntity.setFirstName(null);
		assertEquals("LastName MiddleName", contragentEntity.getPersonFullName());
		contragentEntity.setFirstName("");
		assertEquals("LastName MiddleName", contragentEntity.getPersonFullName());

		contragentEntity.setLastName(null);
		assertEquals("MiddleName", contragentEntity.getPersonFullName());
		contragentEntity.setLastName("");
		assertEquals("MiddleName", contragentEntity.getPersonFullName());

		contragentEntity.setMiddleName(null);
		assertEquals("", contragentEntity.getPersonFullName());
		contragentEntity.setMiddleName("");
		assertEquals("", contragentEntity.getPersonFullName());

	}

	private class TestContragent extends AbstractContragentEntity {

	}

}