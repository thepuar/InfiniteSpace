package es.thepuar.InfiniteSpace.model;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
public class MapEntryPhoto implements Comparable<MapEntryPhoto> {


    @Id
    @GeneratedValue
    private Long Id;

    private Integer parte;
    private String mediaId;
    @Column(length = 1000)
    private String url;
    private Long bytes;

    @ManyToOne
    Fichero fichero;

    public String getUrl1920() {
        return this.getUrl() + "=w1920-h1080";
    }


    @Override
    public int compareTo(MapEntryPhoto o) {
        if (this.getParte() < o.getParte()) {
            return -1;
        } else if (this.getParte().equals(o.getParte())) {
            return 0;
        } else return 1;
    }
}
