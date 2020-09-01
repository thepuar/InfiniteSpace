package es.thepuar.InfiniteSpace.service.impl;

import es.thepuar.InfiniteSpace.dao.EpisodioDAO;
import es.thepuar.InfiniteSpace.dao.SeriesDAO;
import es.thepuar.InfiniteSpace.model.Episodio;
import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.service.api.EpisodioService;
import es.thepuar.InfiniteSpace.service.api.FicheroService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EpisodioServiceImpl implements EpisodioService {

    @Autowired
    EpisodioDAO dao;

    @Autowired
    FicheroService ficheroService;

    @Override
    public Episodio findById(Long id) {
        return dao.findById(id).get();
    }

    @Override
    public List<Episodio> findAll() {
        return dao.findAll();
    }

    @Override
    public void delete(Episodio episodio) {
        dao.delete(episodio);
    }

    @Override
    public void download(Episodio episodio) {
        ficheroService.downloadFile(episodio.getFichero());
    }




    @Override
    public void save(Episodio episodio) {
        dao.save(episodio);
    }

    @Override
    public Episodio crearEpisodio(Fichero fichero) {
       Episodio episodio = new Episodio();
       episodio.setFichero(fichero);
       episodio.setNombre(fichero.getNombre());
       episodio = putNumberEpisode(episodio);
       return episodio;
    }

    private Episodio putNumberEpisode(Episodio episodio){
        String nombre = episodio.getNombre();
        //00x00
        Pattern MY_PATTERN1 = Pattern.compile("\\d*x\\d*");
        // 000
        Pattern MY_PATTERN2 = Pattern.compile("\\d\\d\\d");
        Matcher matcher = MY_PATTERN1.matcher(nombre);

        if (matcher.find()) {
            // 00x00
            String coincidencia = matcher.group();
            if (coincidencia != null) {
                String[] tokens = coincidencia.split("x");
                String numEpisodio = tokens[1];
                episodio = new Episodio();
                episodio.setNumero(Integer.valueOf(numEpisodio));
            }
        }else{
            matcher = MY_PATTERN2.matcher(nombre);
            if(matcher.find()){
                String coincidencia = matcher.group();
                if (coincidencia != null) {
                    episodio = new Episodio();
                    episodio.setNumero(Integer.valueOf(coincidencia));
                }
            }
        }

        return episodio;

    }
}
