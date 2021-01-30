package ua.com.vetal.entity;

import lombok.Data;
import ua.com.vetal.entity.common.AbstractDirectoryEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "chromaticity")
@Data
public class ChromaticityDirectory extends AbstractDirectoryEntity {

}