package es.thepuar.InfiniteSpace.service.api;

import java.io.File;
import java.util.List;

import es.thepuar.InfiniteSpace.model.Fichero;

public interface FicheroService {
	
	public Fichero findById(Long id);
	
	public void save(Fichero fichero);
	
	public List<Fichero> findAll();
	
	public void delete(Fichero fichero);
	
	public void completeFichero(Fichero fichero);
	
	public void downloadFile(Fichero fichero);
	
	public void uploadFile(Fichero fichero);
	
	public Fichero fileToFichero(File f);

	public void compareFile(String fileA, String fileB);

}
