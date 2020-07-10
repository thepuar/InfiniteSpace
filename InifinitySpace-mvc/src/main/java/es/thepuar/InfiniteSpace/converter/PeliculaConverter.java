package es.thepuar.InfiniteSpace.converter;

import es.thepuar.InfiniteSpace.model.Pelicula;
import es.thepuar.InfiniteSpace.service.api.PeliculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PeliculaConverter implements Converter<String, Pelicula> {

    @Autowired
    PeliculaService peliculaService;

    @Override
    public Pelicula convert(String id) {

        return peliculaService.findById(Long.valueOf(id));
    }
}
