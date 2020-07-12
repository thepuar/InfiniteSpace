package es.thepuar.InfiniteSpace.service.api;

import es.thepuar.InfiniteSpace.model.Episodio;
import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.Pelicula;

import java.util.List;

public interface EpisodioService {

    public Episodio findById(Long id);

    public List<Episodio> findAll();

    public void delete(Episodio episodio);

    public void download(Episodio episodio);

    public Episodio crearEpisodio(Fichero fichero);

    public void save(Episodio episodio);
}
