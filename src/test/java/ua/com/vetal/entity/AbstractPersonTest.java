package ua.com.vetal.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AbstractPersonTest {
	private Contractor contractor;

	@Test
	public void getFullName() {
		Contractor contractor = new Contractor();
		contractor.setCorpName("corpName");
		contractor.setShortName("shortName");
		contractor.setFirstName("firstName");
		contractor.setLastName("lastName");
		contractor.setMiddleName("middleName");

		assertEquals(contractor.getCorpName(), contractor.getFullName());
		contractor.setCorpName(null);
		assertEquals("lastName firstName middleName", contractor.getFullName());
        contractor.setCorpName("");
        assertEquals("lastName firstName middleName", contractor.getFullName());

        contractor.setLastName(null);
        assertEquals("firstName middleName", contractor.getFullName());
        contractor.setLastName("");
        assertEquals("firstName middleName", contractor.getFullName());
        contractor.setLastName("lastName");

        contractor.setFirstName(null);
        assertEquals("lastName middleName", contractor.getFullName());
        contractor.setFirstName("");
        assertEquals("lastName middleName", contractor.getFullName());
        contractor.setFirstName("firstName");

        contractor.setMiddleName(null);
        assertEquals("lastName firstName", contractor.getFullName());
        contractor.setMiddleName("");
        assertEquals("lastName firstName", contractor.getFullName());
    }

}