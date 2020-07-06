package es.thepuar.InfiniteSpace.dao;

import es.thepuar.InfiniteSpace.model.Fichero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface FicheroDAO extends JpaRepository<Fichero, Long> {
	

}
