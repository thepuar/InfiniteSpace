package es.thepuar.InfiniteSpace.service.api;

import es.thepuar.InfiniteSpace.model.FicheroDirectorio;

import java.io.File;

public interface ArchivoService {

    public FicheroDirectorio getFicheroDirectorio(int index);

    public File getFile(int index);
}
