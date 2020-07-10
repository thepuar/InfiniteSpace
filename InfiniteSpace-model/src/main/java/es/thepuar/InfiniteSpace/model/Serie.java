package es.thepuar.InfiniteSpace.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Serie {

    @javax.persistence.Id
    @GeneratedValue
    private Long Id;
    private Long id_imdb;
    private String nombre;

}
