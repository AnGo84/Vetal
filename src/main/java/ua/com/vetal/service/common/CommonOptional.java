package ua.com.vetal.service.common;

import java.util.ArrayList;
import java.util.List;

public class CommonOptional<E> {
    public List<E> getListFromIterable(Iterable<E> iterable) {
        List<E> result = new ArrayList<>();
        iterable.forEach(result::add);
        return result;
    }
}
