package es.thepuar.InfiniteSpace.model;

import lombok.Data;

@Data
public class Referencia {

	MapEntryPhoto entry;
	String ruta;
	
	public Referencia(String ruta, MapEntryPhoto entry) {
		this.ruta = ruta;
		this.entry = entry;
	}
}
