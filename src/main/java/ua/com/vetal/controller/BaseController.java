package ua.com.vetal.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.vetal.entity.filter.ViewFilter;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class BaseController {
    private final String controllerName;
    private final Map<String, ViewFilter> viewFilterMap;
    private final ViewFilter defaultFilter;

    public ViewFilter getViewFilter() {
        initViewFilter();
        return viewFilterMap.get(controllerName);
    }

    public ViewFilter updateViewFilter(ViewFilter viewFilter) {
        return viewFilterMap.put(controllerName, viewFilter);
    }

    public void initViewFilter() {
        viewFilterMap.putIfAbsent(controllerName, defaultFilter.getDefault());
    }
}
