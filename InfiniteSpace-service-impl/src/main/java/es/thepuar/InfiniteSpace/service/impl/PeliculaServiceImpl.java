package es.thepuar.InfiniteSpace.service.impl;

import es.thepuar.InfiniteSpace.dao.PeliculaDAO;
import es.thepuar.InfiniteSpace.model.Pelicula;
import es.thepuar.InfiniteSpace.service.api.FicheroService;
import es.thepuar.InfiniteSpace.service.api.PeliculaService;
import es.thepuar.InfiniteSpace.utils.ImdbUtil;
import es.thepuar.InfiniteSpace.utils.XugglerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PeliculaServiceImpl implements PeliculaService {

    @Autowired
    PeliculaDAO dao;

    @Autowired
    FicheroService ficheroService;

    @Override
    public Pelicula findById(Long id) {
        return dao.findById(id).get();
    }

    @Override
    public List<Pelicula> findAll() {
        return dao.findAll();
    }

    @Override
    public void delete(Pelicula pelicula) {
        dao.delete(pelicula);
        ficheroService.delete(pelicula.getFichero());

    }

    @Override
    public void download(Pelicula pelicula) {
        ficheroService.downloadFile(pelicula.getFichero());
    }

    @Override
    public void save(Pelicula pelicula) {
        dao.save(pelicula);
    }

    @Override
    public void completePelicula(Pelicula pelicula) {
        pelicula.setSegundos(XugglerUtil.getVideoSeconds(pelicula.getFichero().getFile()));
        pelicula.setAncho(XugglerUtil.getVideoWidth(pelicula.getFichero().getFile()));
        pelicula.setAlto(XugglerUtil.getVideoHeight(pelicula.getFichero().getFile()));
        pelicula.setCaratula(ImdbUtil.getCover(pelicula.getNombre()));
    }

}
