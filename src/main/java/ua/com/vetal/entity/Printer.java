package ua.com.vetal.entity;

import ua.com.vetal.entity.common.AbstractEmployeeEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "printers")
public class Printer extends AbstractEmployeeEntity {

}