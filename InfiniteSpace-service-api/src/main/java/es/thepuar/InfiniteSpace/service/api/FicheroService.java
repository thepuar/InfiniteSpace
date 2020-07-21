package es.thepuar.InfiniteSpace.service.api;

import es.thepuar.InfiniteSpace.model.Fichero;

import java.io.File;
import java.util.List;


public interface FicheroService {
	
	public Fichero findById(Long id);
	
	public void save(Fichero fichero);
	
	public List<Fichero> findAll();
	
	public void delete(Fichero fichero);
	
	public void completeFichero(Fichero fichero);
	
	public void downloadFile(Fichero fichero);
	
	public void uploadFile(Fichero fichero);

	//public void uploadFileMax(File f);
	
	public Fichero fileToFichero(File f);

	public void compareFile(String fileA, String fileB);

	public boolean esPosibleDescargar(Fichero fichero);

	public List<Fichero> findAllAloneFichero();

	public boolean isVideo(Fichero fichero);



}
