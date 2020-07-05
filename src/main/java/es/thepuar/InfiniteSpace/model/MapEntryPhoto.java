package es.thepuar.InfiniteSpace.model;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
public class MapEntryPhoto {

	
	@Id
	@GeneratedValue
	private Long Id;
	
	private Integer parte;
	private String mediaId;
	@Column(length=1000)
	private String url;
	private Long bytes;
	
	@ManyToOne
	Fichero fichero;

	public String getUrl1920(){
		return this.getUrl()+"=w1920-h1080";
	}
}
