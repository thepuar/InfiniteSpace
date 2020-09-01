package es.thepuar.InfiniteSpace.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Data
@Entity
public class Pelicula {

    @Id
    @GeneratedValue
    private Long Id;
    private Long id_imdb;
    private String nombre;
    private Integer anyo;
    private Integer alto;
    private Integer ancho;
    private Integer segundos;
    private String caratula;

    @OneToOne
    private Fichero fichero;

    public String getDuration() {
        int sec = this.getSegundos();

        Date d = new Date(sec * 1000L);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss"); // HH for 0-23
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        String time = df.format(d);
        return time;
    }
}
