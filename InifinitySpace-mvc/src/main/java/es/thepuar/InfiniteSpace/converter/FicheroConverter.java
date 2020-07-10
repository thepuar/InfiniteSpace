package es.thepuar.InfiniteSpace.converter;

import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.service.api.FicheroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FicheroConverter implements Converter<String, Fichero> {

    @Autowired
    FicheroService ficheroService;

    @Override
    public Fichero convert(String id) {
        return this.ficheroService.findById(Long.valueOf(id));
    }
}
