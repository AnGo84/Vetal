package ua.com.vetal.controller.common;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.com.vetal.entity.common.AbstractEntity;

import javax.validation.Valid;

public interface CommonController<E extends AbstractEntity> {

    @GetMapping(value = {"", "/list", "/all"})
    String allRecords(Model model);

    @GetMapping(value = {"/add"})
    String addRecord(Model model);

    @GetMapping(value = "/edit-{id}")
    String editRecord(@PathVariable Long id, Model model);

    @PostMapping(value = "/update")
    String updateRecord(@Valid E directory,
                        BindingResult bindingResult, Model model);

    @GetMapping(value = {"/delete-{id}"})
    String deleteRecord(@PathVariable Long id);

    default E createInstance(Class<E> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
