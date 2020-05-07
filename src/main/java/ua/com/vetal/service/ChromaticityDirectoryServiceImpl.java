package ua.com.vetal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.ChromaticityDirectory;
import ua.com.vetal.repositories.ChromaticityDirectoryRepository;
import ua.com.vetal.service.common.AbstractDirectoryService;

@Service("chronomaticityDirectoryService")
@Transactional

public class ChromaticityDirectoryServiceImpl extends AbstractDirectoryService<ChromaticityDirectory, ChromaticityDirectoryRepository> {
    public ChromaticityDirectoryServiceImpl(ChromaticityDirectoryRepository repository) {
        super(repository);
    }


/*public class ChromaticityDirectoryServiceImpl implements SimpleService<ChromaticityDirectory> {


    @Autowired
    private ChromaticityDirectoryRepository directoryRepository;

    @Override
    public ChromaticityDirectory findById(Long id) {
        */
    /*
     * Optional<User> optinalEntity = userRepository.findById(id); User user
     * = optinalEntity.get(); return user;
     *//*

        return directoryRepository.getOne(id);
    }

    @Override
    public ChromaticityDirectory findByName(String name) {
        return directoryRepository.findByName(name);
    }

    @Override
    public void saveObject(ChromaticityDirectory directory) {
        directoryRepository.save(directory);
    }

    @Override
    public void updateObject(ChromaticityDirectory directory) {
        saveObject(directory);
    }

    @Override
    public void deleteById(Long id) {
        directoryRepository.deleteById(id);
    }

    @Override
    public List<ChromaticityDirectory> findAllObjects() {
        return directoryRepository.findAll();
    }

    @Override
    public boolean isObjectExist(ChromaticityDirectory directory) {
        return findByName(directory.getName()) != null;
    }
*/

}
