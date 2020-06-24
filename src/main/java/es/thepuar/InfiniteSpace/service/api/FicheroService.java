package es.thepuar.InfiniteSpace.service.api;

import java.util.List;

import es.thepuar.InfiniteSpace.model.Fichero;

public interface FicheroService {
	
	public void save(Fichero fichero);
	
	public List<Fichero> findAll();
	
	public void completeFichero(Fichero fichero);

}
