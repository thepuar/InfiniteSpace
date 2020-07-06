package es.thepuar.InfiniteSpace.dao;

import es.thepuar.InfiniteSpace.model.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeliculaDAO extends JpaRepository<Pelicula, Long> {
}
