package ua.com.vetal.controller;

import org.junit.jupiter.api.Test;
import ua.com.vetal.entity.filter.ClientViewFilter;
import ua.com.vetal.entity.filter.PersonViewFilter;
import ua.com.vetal.entity.filter.ViewFilter;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class BaseControllerTest {
    private BaseController baseController;

    @Test
    public void WhenGetViewFilter_thenOk() {
        baseController = new BaseController("Controller", new HashMap<>(), new ClientViewFilter());
        ViewFilter viewFilter = baseController.getViewFilter();
        assertNotNull(viewFilter);
        assertTrue(viewFilter.getClass() == ClientViewFilter.class);
        assertEquals(new ClientViewFilter(), viewFilter);
    }

    @Test
    public void WhenGetViewFilter_thenThrowNPE() {
        baseController = new BaseController("Controller", new HashMap<>(), null);
        assertThrows(NullPointerException.class, () -> {
            baseController.getViewFilter();
        });
    }

    @Test
    public void onUpdateViewFilter() {
        baseController = new BaseController("Controller", new HashMap<>(), new PersonViewFilter());
        PersonViewFilter personViewFilter = new PersonViewFilter();
        personViewFilter.setCorpName("CorpName");
        baseController.updateViewFilter(personViewFilter);
        assertEquals(personViewFilter, baseController.getViewFilter());

        baseController.updateViewFilter(null);
        assertNotNull(baseController.getViewFilter());
        assertEquals(new PersonViewFilter(), baseController.getViewFilter());
    }

}