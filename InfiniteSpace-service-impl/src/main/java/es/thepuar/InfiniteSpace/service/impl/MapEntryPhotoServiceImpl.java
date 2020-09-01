package es.thepuar.InfiniteSpace.service.impl;

import java.util.Collections;
import java.util.List;

import es.thepuar.InfiniteSpace.google.client.PhotoClientJava;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.thepuar.InfiniteSpace.dao.MapEntryPhotosDAO;
import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.MapEntryPhoto;
import es.thepuar.InfiniteSpace.service.api.MapEntryPhotoService;

@Service
public class MapEntryPhotoServiceImpl implements MapEntryPhotoService {

    @Autowired
    MapEntryPhotosDAO dao;

    @Autowired
    PhotoClientJava photoClient;

    @Override
    public void save(MapEntryPhoto mapEntryPhoto) {
        dao.save(mapEntryPhoto);

    }

    @Override
    public List<MapEntryPhoto> findAll() {
        return dao.findAll();
    }

    @Override
    public void delete(MapEntryPhoto mapEntryPhoto) {
        dao.delete(mapEntryPhoto);

    }

    @Override
    public List<MapEntryPhoto> findByFichero(Fichero fichero) {
        List<MapEntryPhoto> partes =dao.findByFichero(fichero);
        Collections.sort(partes);
        return partes;
    }

    @Override
    public boolean esPosibleDescargar(MapEntryPhoto entry) {

        String baseUrl = this.photoClient.getBaseUrl(entry);

        if (!StringUtils.isBlank(baseUrl)) {
            entry.setUrl(baseUrl);
            this.save(entry);
            return true;
        } else
            return false;

    }


}
