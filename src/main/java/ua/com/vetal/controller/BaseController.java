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
    //private ViewFilter viewFilter;

    public ViewFilter getViewFilter() {
        ViewFilter viewFilter = viewFilterMap.get(controllerName);
        if (viewFilter == null) {
            viewFilter = defaultFilter.getDefault();
            viewFilterMap.put(controllerName, viewFilter);
        }
        return viewFilter;
    }

    public ViewFilter updateViewFilter(ViewFilter viewFilter) {
        viewFilter = viewFilterMap.put(controllerName, viewFilter);

        return viewFilter;
    }

    /*public void initViewFilter(ViewFilter viewFilter) {
        if (!viewFilterMap.containsKey(controllerName)) {
            viewFilterMap.put(controllerName, viewFilter);
        }
    }*/
}
