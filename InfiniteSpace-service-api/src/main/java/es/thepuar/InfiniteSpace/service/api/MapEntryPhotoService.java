package es.thepuar.InfiniteSpace.service.api;

import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.MapEntryPhoto;

import java.util.List;



public interface MapEntryPhotoService {
	
	public void save(MapEntryPhoto mapEntryPhoto);
	
	public List<MapEntryPhoto> findAll();
	
	public void delete(MapEntryPhoto mapEntryPhoto);
	
	public List<MapEntryPhoto> findByFichero(Fichero fichero);

	public boolean esPosibleDescargar(MapEntryPhoto entry);

}
