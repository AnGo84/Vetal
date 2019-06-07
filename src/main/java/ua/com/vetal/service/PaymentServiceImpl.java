package ua.com.vetal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.Payment;
import ua.com.vetal.repositories.PaymentRepository;

import java.util.List;

@Service("paymentService")
@Transactional
public class PaymentServiceImpl implements SimpleService<Payment> {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment findById(Long id) {
        return paymentRepository.getOne(id);
    }

    @Override
    public Payment findByName(String name) {
        return paymentRepository.findByName(name);
    }

    @Override
    public void saveObject(Payment object) {
        paymentRepository.save(object);
    }

    @Override
    public void updateObject(Payment object) {
        saveObject(object);
    }

    @Override
    public void deleteById(Long id) {
        paymentRepository.deleteById(id);
    }

    @Override
    public List<Payment> findAllObjects() {
        return paymentRepository.findAll();
    }

    @Override
    public boolean isObjectExist(Payment object) {
        return findByName(object.getName()) != null;
    }
}
