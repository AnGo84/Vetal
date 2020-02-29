package ua.com.vetal.entity.filter;

import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ua.com.vetal.entity.Manager;

@Component
@Scope("prototype")
public class ClientFilter {
    private String fullName;
    private Manager manager;

    public boolean hasData() {
        if (!Strings.isBlank(fullName)) {
            return true;
        } else if (manager != null) {
            return true;
        }
        return false;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
        if (!(o instanceof ClientFilter)) return false;

        ClientFilter that = (ClientFilter) o;

        if (fullName != null ? !fullName.equals(that.fullName) : that.fullName != null) return false;
        return manager != null ? manager.equals(that.manager) : that.manager == null;
    }

    @Override
    public int hashCode() {
        int result = fullName != null ? fullName.hashCode() : 0;
        result = 31 * result + (manager != null ? manager.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ClientFilter{");
        sb.append("fullName='").append(fullName).append('\'');
        sb.append(", manager=").append(manager);
        sb.append('}');
        return sb.toString();
    }
}
