package ua.com.vetal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.FormatDirectory;
import ua.com.vetal.entity.PrintingUnitDirectory;
import ua.com.vetal.repositories.FormatDirectoryRepository;
import ua.com.vetal.repositories.PrintingUnitDirectoryRepository;

import java.util.List;

@Service("printingUnitDirectoryService")
@Transactional
public class PrintingUnitDirectoryServiceImpl implements SimpleService<PrintingUnitDirectory> {

	// private static final Logger logger =
	// LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private PrintingUnitDirectoryRepository printingUnitRepository;

	@Override
	public PrintingUnitDirectory findById(Long id) {
		/*
		 * Optional<User> optinalEntity = userRepository.findById(id); User user
		 * = optinalEntity.get(); return user;
		 */
		return printingUnitRepository.getOne(id);
	}

	@Override
	public PrintingUnitDirectory findByName(String name) {
		return printingUnitRepository.findByName(name);
	}

	@Override
	public void saveObject(PrintingUnitDirectory directory) {
		printingUnitRepository.save(directory);
	}

	@Override
	public void updateObject(PrintingUnitDirectory directory) {
		saveObject(directory);
	}

	@Override
	public void deleteById(Long id) {
		printingUnitRepository.deleteById(id);
	}

	@Override
	public List<PrintingUnitDirectory> findAllObjects() {
		return printingUnitRepository.findAll();
	}

	@Override
	public boolean isObjectExist(PrintingUnitDirectory directory) {
		return findByName(directory.getName()) != null;
	}

}
