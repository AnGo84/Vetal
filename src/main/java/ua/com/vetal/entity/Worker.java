package ua.com.vetal.entity;

import ua.com.vetal.entity.common.AbstractEmployeeEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "workers")
public class Worker extends AbstractEmployeeEntity {
}
