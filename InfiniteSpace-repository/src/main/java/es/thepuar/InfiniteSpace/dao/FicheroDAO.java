package es.thepuar.InfiniteSpace.dao;

import es.thepuar.InfiniteSpace.model.Fichero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FicheroDAO extends JpaRepository<Fichero, Long> {


    @Query("select f from Fichero f where f.id not in (select p.fichero.id from Pelicula p)")
public List<Fichero> findAllAloneFichero();
}
