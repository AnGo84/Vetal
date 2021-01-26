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
