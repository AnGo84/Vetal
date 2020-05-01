package ua.com.vetal.entity.filter;

import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import ua.com.vetal.entity.Manager;

@Data
public class PersonViewFilter implements ViewFilter {
    private String corpName;
    private Manager manager;

    @Override
    public boolean hasData() {
        if (!Strings.isBlank(corpName)) {
            return true;
        } else if (manager != null) {
            return true;
        }
        return false;
    }

    @Override
    public ViewFilter getDefault() {
        return new PersonViewFilter();
    }
}
