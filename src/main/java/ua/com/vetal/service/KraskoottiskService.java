package ua.com.vetal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
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

    public double getKraskoottiskAmount() {
        List<Kraskoottisk> list = kraskoottiskRepository.findAll();
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        return list.get(0).getAmount();
    }

}
