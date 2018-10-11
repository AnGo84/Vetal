package ua.com.vetal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.LinkType;
import ua.com.vetal.repositories.LinkTypeRepository;

import java.util.List;

@Service("linkTypeService")
@Transactional
public class LinkTypeServiceImpl implements SimpleService<LinkType> {

	@Autowired
	private LinkTypeRepository linkTypeRepository;

	@Override
	public LinkType findById(Long id) {
		return linkTypeRepository.getOne(id);
	}

	@Override
	public LinkType findByName(String name) {
		return linkTypeRepository.findByName(name);
	}

	@Override
	public void saveObject(LinkType linkType) {
		linkTypeRepository.save(linkType);
	}

	@Override
	public void updateObject(LinkType linkType) {
		saveObject(linkType);
	}

	@Override
	public void deleteById(Long id) {
		linkTypeRepository.deleteById(id);
	}

	@Override
	public List<LinkType> findAllObjects() {
		return linkTypeRepository.findAll();
	}

	@Override
	public boolean isObjectExist(LinkType linkType) {
		return findByName(linkType.getName()) != null;
	}

}
