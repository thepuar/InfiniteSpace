package es.thepuar.InfiniteSpace.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class MapEntryPhoto {

	
	@Id
	@GeneratedValue
	private Long Id;
	
	private Integer parte;
	private String mediaId;
	private String url;
	
	@ManyToOne
	Fichero fichero;
	
}
