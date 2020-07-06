package es.thepuar.InfiniteSpace.service.api;

import es.thepuar.InfiniteSpace.model.Pelicula;

import java.util.List;

public interface PeliculaService {

    public Pelicula findById(Long id);

    public List<Pelicula> findAll();

    public void delete(Pelicula pelicula);

    public void download(Pelicula pelicula);

    public void save(Pelicula pelicula);
}
