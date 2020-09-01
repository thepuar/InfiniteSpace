package es.thepuar.InfiniteSpace.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Episodio {

    @Id
    @GeneratedValue
    private Long Id;
    private String nombre;
    private Integer numero;

    @ManyToOne
    private Serie serie;

    @OneToOne
    private Fichero fichero;
}
