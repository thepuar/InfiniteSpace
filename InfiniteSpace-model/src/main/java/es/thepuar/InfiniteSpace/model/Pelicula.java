package es.thepuar.InfiniteSpace.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Data
@Entity
public class Pelicula {

    @javax.persistence.Id
    @GeneratedValue
    private Long Id;
    private Long id_imdb;
    private String nombre;
    private Integer anyo;

    @OneToOne
    private Fichero fichero;
}
