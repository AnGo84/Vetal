package ua.com.vetal.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.dao.StencilDAO;
import ua.com.vetal.entity.Stencil;
import ua.com.vetal.entity.filter.OrderViewFilter;
import ua.com.vetal.repositories.StencilRepository;

import java.util.List;

@Service("stencilService")
@Transactional
@Slf4j
public class StencilServiceImpl implements SimpleService<Stencil> {
    @Autowired
    private StencilDAO stencilDAO;
    @Autowired
    private StencilRepository stencilRepository;

    @Override
    public Stencil findById(Long id) {
        return stencilRepository.getOne(id);
    }

    @Override
    public Stencil findByName(String name) {
        return null;
    }

    @Override
    public void saveObject(Stencil stencil) {
        stencilRepository.save(stencil);
    }

    @Override
    public void updateObject(Stencil stencil) {
        saveObject(stencil);
    }

    @Override
    public void deleteById(Long id) {
        stencilRepository.deleteById(id);
    }

    @Override
    public List<Stencil> findAllObjects() {
        List<Stencil> getList = stencilRepository.findAll(sortByDateBeginDesc());
        return getList;
    }

    public Stencil findByAccount(String account) {
        return stencilRepository.findByAccount(account);
    }

    public List<Stencil> findByFilterData(OrderViewFilter orderViewFilter) {
        if (orderViewFilter == null) {
            return findAllObjects();
        }
        List<Stencil> list = stencilDAO.findByFilterData(orderViewFilter);
        return list;
    }

    @Override
    public boolean isObjectExist(Stencil stencil) {
        return findById(stencil.getId()) != null;
    }

    private Sort sortByDateBeginDesc() {
        return Sort.by(Sort.Direction.DESC, "dateBegin");
    }

    public boolean isAccountValueExist(Stencil stencil) {
        Stencil findStencil = findByAccount(stencil.getAccount());
        return (findStencil != null && findStencil.getId() != null && !findStencil.getId().equals(stencil.getId()));
    }

    public Long getMaxID() {
        return stencilDAO.getMaxID();
    }
}
