package es.thepuar.InfiniteSpace.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.MapEntryPhoto;

@Repository
public interface MapEntryPhotosDAO extends JpaRepository<MapEntryPhoto, Long> {
	
	public List<MapEntryPhoto> findByFichero(Fichero fichero);

}
