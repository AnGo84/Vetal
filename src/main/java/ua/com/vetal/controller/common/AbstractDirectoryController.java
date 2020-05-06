package ua.com.vetal.controller.common;

import lombok.RequiredArgsConstructor;
import ua.com.vetal.entity.AbstractEntity;
import ua.com.vetal.service.common.CommonService;

@RequiredArgsConstructor
public abstract class AbstractDirectoryController<E extends AbstractEntity, S extends CommonService<E>>
        implements CommonController<E> {

    private final S service;


}
