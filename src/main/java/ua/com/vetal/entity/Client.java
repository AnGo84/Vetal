package ua.com.vetal.entity;

import ua.com.vetal.entity.common.AbstractContragentEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "clients")
public class Client extends AbstractContragentEntity {

}
