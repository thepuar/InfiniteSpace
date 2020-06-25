package es.thepuar.InfiniteSpace.service.api;

import java.util.List;

import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.MapEntryPhoto;

public interface MapEntryPhotoService {
	
	public void save(MapEntryPhoto mapEntryPhoto);
	
	public List<MapEntryPhoto> findAll();
	
	public void delete(MapEntryPhoto mapEntryPhoto);
	
	public List<MapEntryPhoto> findByFichero(Fichero fichero);

}
