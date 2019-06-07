package ua.com.vetal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.PaperDirectory;
import ua.com.vetal.repositories.PaperDirectoryRepository;

import java.util.List;

@Service("paperDirectoryService")
@Transactional
public class PaperDirectoryServiceImpl implements SimpleService<PaperDirectory> {

    // private static final Logger logger =
    // LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private PaperDirectoryRepository directoryRepository;

    @Override
    public PaperDirectory findById(Long id) {
        /*
         * Optional<User> optinalEntity = userRepository.findById(id); User user
         * = optinalEntity.get(); return user;
         */
        return directoryRepository.getOne(id);
    }

    @Override
    public PaperDirectory findByName(String name) {
        return directoryRepository.findByName(name);
    }

    @Override
    public void saveObject(PaperDirectory directory) {
        directoryRepository.save(directory);
    }

    @Override
    public void updateObject(PaperDirectory directory) {
        saveObject(directory);
    }

    @Override
    public void deleteById(Long id) {
        directoryRepository.deleteById(id);
    }

    @Override
    public List<PaperDirectory> findAllObjects() {
        return directoryRepository.findAll();
    }

    @Override
    public boolean isObjectExist(PaperDirectory directory) {
        return findByName(directory.getName()) != null;
    }

}
