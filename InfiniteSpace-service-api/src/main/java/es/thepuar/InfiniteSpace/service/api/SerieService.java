package es.thepuar.InfiniteSpace.service.api;

import es.thepuar.InfiniteSpace.model.Serie;

import java.io.File;
import java.util.List;

public interface SerieService {

    public Serie findById(Long id);

    public List<Serie> findAll();

    public void delete(Serie serie);

    public void save(Serie serie);

    public void uploadDirectory(File directory, Serie serie);
}
