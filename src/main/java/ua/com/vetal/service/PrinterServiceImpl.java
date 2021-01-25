package ua.com.vetal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.Printer;
import ua.com.vetal.repositories.PrinterRepository;
import ua.com.vetal.service.common.AbstractEmployeeService;

@Service("printerService")
@Transactional
public class PrinterServiceImpl extends AbstractEmployeeService<Printer, PrinterRepository> {

    public PrinterServiceImpl(PrinterRepository repository) {
        super(repository);
    }
}
/*
public class PrinterServiceImpl implements SimpleService<Printer> {

    // private static final Logger logger =
    // LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private PrinterRepository personRepository;

    @Override
    public Printer findById(Long id) {
        */
/*
 * Optional<User> optinalEntity = userRepository.findById(id); User user
 * = optinalEntity.get(); return user;
 *//*

        return personRepository.getOne(id);
    }

    @Override
    public Printer findByName(String name) {
        // return directoryRepository.findByName(name);
        return null;
    }

    @Override
    public void saveObject(Printer person) {
        personRepository.save(person);
    }

    @Override
    public void updateObject(Printer person) {
        saveObject(person);
    }

    @Override
    public void deleteById(Long id) {
        personRepository.deleteById(id);
    }

    @Override
    public List<Printer> findAllObjects() {
        return personRepository.findAll();
    }

    @Override
    public boolean isObjectExist(Printer person) {
        return findById(person.getId()) != null;
    }

}
*/
