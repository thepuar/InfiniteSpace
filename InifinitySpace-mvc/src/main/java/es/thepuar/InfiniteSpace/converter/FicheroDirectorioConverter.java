package es.thepuar.InfiniteSpace.converter;

import es.thepuar.InfiniteSpace.model.FicheroDirectorio;
import es.thepuar.InfiniteSpace.service.api.ArchivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FicheroDirectorioConverter implements Converter<String, FicheroDirectorio> {

    @Autowired
    ArchivoService archivoService;

    @Override
    public FicheroDirectorio convert(String id) {
        FicheroDirectorio ficheroDirectorio = null;
        ficheroDirectorio = archivoService.getFicheroDirectorio(Integer.valueOf(id));
        return ficheroDirectorio;
    }
}
