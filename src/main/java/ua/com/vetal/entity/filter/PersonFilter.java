package ua.com.vetal.entity.filter;

import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ua.com.vetal.entity.*;

@Component
@Scope("prototype")
public class PersonFilter {
    private String corpName;
    private Manager manager;

    public boolean hasData() {
        if (!Strings.isBlank(corpName)) {
            return true;
        } else if (manager != null) {
            return true;
        }
        return false;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonFilter)) return false;

        PersonFilter that = (PersonFilter) o;

        if (corpName != null ? !corpName.equals(that.corpName) : that.corpName != null) return false;
        return manager != null ? manager.equals(that.manager) : that.manager == null;
    }

    @Override
    public int hashCode() {
        int result = corpName != null ? corpName.hashCode() : 0;
        result = 31 * result + (manager != null ? manager.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ContractorFilter{");
        sb.append("corpName='").append(corpName).append('\'');
        sb.append(", manager=").append(manager);
        sb.append('}');
        return sb.toString();
    }
}
