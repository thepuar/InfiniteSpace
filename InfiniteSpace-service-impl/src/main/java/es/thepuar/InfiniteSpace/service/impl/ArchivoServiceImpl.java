package es.thepuar.InfiniteSpace.service.impl;

import es.thepuar.InfiniteSpace.manager.ResourceManager;
import es.thepuar.InfiniteSpace.model.FicheroDirectorio;
import es.thepuar.InfiniteSpace.service.api.ArchivoService;
import es.thepuar.InfiniteSpace.utils.Ruta;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service

public class ArchivoServiceImpl implements ArchivoService {

    @Autowired
    private Ruta ruta;

    private String getRuta(){return this.ruta.getRuta();}
    private void setRuta(String ruta ){this.ruta.setRuta(ruta);}


    @Override
    public FicheroDirectorio getFicheroDirectorio(int index){
        if (StringUtils.isBlank(this.getRuta()))
            this.setRuta(ResourceManager.getProperty("ruta_upload"));
        File directorio = new File(this.getRuta());

        if (directorio.isDirectory()) {
            int i = 1;
            for (File file : directorio.listFiles()) {
                if(index==i)
                return new FicheroDirectorio(i, file);
                else i++;
            }
        }
        return null;
    }

    @Override
    public File getFile(int index) {
        return getFicheroDirectorio(index).getFile();
    }
}
