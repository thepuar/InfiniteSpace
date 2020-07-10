package es.thepuar.InfiniteSpace.service.impl;

import es.thepuar.InfiniteSpace.dao.SeriesDAO;
import es.thepuar.InfiniteSpace.model.Serie;
import es.thepuar.InfiniteSpace.service.api.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SerieServiceImpl implements SerieService {

    @Autowired
    SeriesDAO dao;
    @Override
    public Serie findById(Long id) {
        return dao.findById(id).get();
    }

    @Override
    public List<Serie> findAll() {
        return dao.findAll();
    }

    @Override
    public void delete(Serie serie) {
        dao.delete(serie);
    }

    @Override
    public void save(Serie serie) {
        dao.save(serie);
    }
}
