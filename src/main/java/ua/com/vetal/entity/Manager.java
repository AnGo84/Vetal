package ua.com.vetal.entity;

import ua.com.vetal.entity.common.AbstractEmployeeEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "managers")
public class Manager extends AbstractEmployeeEntity {

}
