package ua.com.vetal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.Link;
import ua.com.vetal.repositories.LinkRepository;

import java.util.List;

@Service("linkService")
@Transactional
public class LinkServiceImpl {

    @Autowired
    private LinkRepository linkRepository;

    public Link findById(Long id) {
        return linkRepository.getOne(id);
    }

    public Link findByName(String name) {
        return linkRepository.findByFullName(name);
    }

    public List<Link> findByLinkTypeId(Long typeId) {
        return linkRepository.findByLinkType_Id(typeId);
    }

    public void saveObject(Link link) {
        linkRepository.save(link);
    }

    public void updateObject(Link link) {
        saveObject(link);
    }

    public void deleteById(Long id) {
        linkRepository.deleteById(id);
    }

    public List<Link> findAllObjects() {
        return linkRepository.findAll();
    }

    public boolean isObjectExist(Link link) {
        return findByName(link.getFullName()) != null;
    }

}
