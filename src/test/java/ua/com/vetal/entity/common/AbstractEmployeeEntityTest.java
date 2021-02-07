package ua.com.vetal.entity.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractEmployeeEntityTest {
	private AbstractEmployeeEntity employeeEntity;

	@BeforeEach
	public void beforeEach() {
		employeeEntity = new AbstractEmployeeEntityTest.TestEmployee();
		employeeEntity.setId(1l);
		employeeEntity.setFirstName("FirstName");
		employeeEntity.setMiddleName("MiddleName");
		employeeEntity.setLastName("LastName");
		employeeEntity.setAddress("Address");
		employeeEntity.setEmail("Email");
		employeeEntity.setPhone("Phone");
	}

	@Test
	public void whenGetPersonFullName() {
		assertEquals("LastName FirstName MiddleName", employeeEntity.getFullName());
		employeeEntity.setFirstName(null);
		assertEquals("LastName MiddleName", employeeEntity.getFullName());
		employeeEntity.setFirstName("");
		assertEquals("LastName MiddleName", employeeEntity.getFullName());

		employeeEntity.setLastName(null);
		assertEquals("MiddleName", employeeEntity.getFullName());
		employeeEntity.setLastName("");
		assertEquals("MiddleName", employeeEntity.getFullName());

		employeeEntity.setMiddleName(null);
		assertEquals("", employeeEntity.getFullName());
		employeeEntity.setMiddleName("");
		assertEquals("", employeeEntity.getFullName());

	}

	private class TestEmployee extends AbstractEmployeeEntity {

	}
}