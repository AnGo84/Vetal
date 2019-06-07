package ua.com.vetal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.Kraskoottisk;
import ua.com.vetal.repositories.KraskoottiskRepository;

import java.util.List;

@Service("kraskoottiskService")
@Transactional
public class KraskoottiskService {

    @Autowired
    private KraskoottiskRepository kraskoottiskRepository;

    public List<Kraskoottisk> findAllObjects() {
        return kraskoottiskRepository.findAll();
    }

}
