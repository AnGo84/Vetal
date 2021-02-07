package ua.com.vetal.entity;

import ua.com.vetal.entity.common.AbstractContragentEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "contractors")
public class Contractor extends AbstractContragentEntity {
}
