package ua.com.vetal.report;

import java.util.List;

public interface Reportable<T, E, K> {
	E getReportData(T object);

	E getReportData(List<T> objects);

	E getReportData(List<T> objects, K filterData);
}
