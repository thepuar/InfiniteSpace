package es.thepuar.InfiniteSpace.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.thepuar.InfiniteSpace.model.Fichero;

@Repository
public interface FicheroDAO extends JpaRepository<Fichero, Long> {
	

}
