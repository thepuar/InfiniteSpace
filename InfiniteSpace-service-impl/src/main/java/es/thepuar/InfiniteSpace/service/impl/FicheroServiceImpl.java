package es.thepuar.InfiniteSpace.service.impl;

import es.thepuar.InfiniteSpace.dao.FicheroDAO;
import es.thepuar.InfiniteSpace.google.client.PhotoClientJava;
import es.thepuar.InfiniteSpace.manager.RequestUriManager;
import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.MapEntryPhoto;
import es.thepuar.InfiniteSpace.model.Referencia;
import es.thepuar.InfiniteSpace.service.api.FicheroService;
import es.thepuar.InfiniteSpace.service.api.FileToPng;
import es.thepuar.InfiniteSpace.service.api.MapEntryPhotoService;
import es.thepuar.InfiniteSpace.utils.PrinterUtil;
import es.thepuar.InfiniteSpace.utils.XugglerUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Calendar;
import java.util.List;

@Service
public class FicheroServiceImpl implements FicheroService {
    private static final Logger logger = LogManager.getLogger(FicheroServiceImpl.class);
    @Autowired
    FicheroDAO dao;

    @Autowired
    MapEntryPhotoService mapEntryPhotoService;

    @Autowired
    PhotoClientJava photoClient;

    @Autowired
    FileToPng fileToPngImplV2;

    @Autowired
    RequestUriManager  requestUriManager;

    @Override
    public void save(Fichero fichero) {
        dao.save(fichero);

    }

    @Override
    public List<Fichero> findAll() {
        return dao.findAll();
    }

    @Override
    public void completeFichero(Fichero fichero) {
        if (StringUtils.isBlank(fichero.getNombre())) {
            fichero.setNombre(this.getNombreFileName(fichero.getFile().getName()));
        }

        if (StringUtils.isBlank(fichero.getExtension())) {
            fichero.setExtension(this.getExtensionFileName(fichero.getFile().getName()));
        }
        fichero.setBytes(fichero.getFile().length());
        fichero.setFechaCreacion(Calendar.getInstance());

    }


    public String getNombreFileName(String fichero) {
        //String[] tokens = fichero.split("\\.");
        int dotPosition = fichero.lastIndexOf('.');
        String nombre = fichero.substring(0,dotPosition);
        return nombre;
    }

    public String getExtensionFileName(String fichero) {
        int dotPosition = fichero.lastIndexOf('.');
        String extension = fichero.substring(dotPosition+1,fichero.length());
        return extension;
    }

    private String getNombrePath(String path) {
        String[] tokens = StringUtils.split("\\");
        return getNombreFileName(tokens[tokens.length - 1]);
    }

    private String getExtensionPath(String path) {
        String[] tokens = StringUtils.split("\\");
        return getExtensionFileName(tokens[tokens.length - 1]);

    }

    @Override
    public void downloadFile(Fichero fichero) {
        List<MapEntryPhoto> partes = this.mapEntryPhotoService.findByFichero(fichero);
        List<Referencia> referencias = this.photoClient.downloadFichero(partes);
        this.fileToPngImplV2.createOriginalFromReferencia(referencias);


    }

    @Override
    public void delete(Fichero fichero) {
        List<MapEntryPhoto> mapEntryPhotos = this.mapEntryPhotoService.findByFichero(fichero);
        for (MapEntryPhoto entry : mapEntryPhotos) {
            this.mapEntryPhotoService.delete(entry);
        }
        dao.delete(fichero);
    }


    @Override
    public void uploadFile(Fichero fichero) {
        List<Referencia> referencias = fileToPngImplV2.convertFichero2Png(fichero);

        for (Referencia referencia : referencias) {
            referencia.getEntry().setFichero(fichero);
            this.mapEntryPhotoService.save(referencia.getEntry());
        }
    }


    @Override
    public Fichero fileToFichero(File file) {
        Fichero fichero = new Fichero();
        fichero.setFile(file);
        this.completeFichero(fichero);
        return fichero;
    }

    @Override
    public void compareFile(String fileA, String fileB) {
        this.fileToPngImplV2.compareFiles("Z:\\App\\InfiniteSpace\\upload\\trincherasruthmalena.PNG", "Z:\\App\\InfiniteSpace\\final\\trincherasruthmalena.PNG");
    }

    /**
     * Recupera las  urls para comprobar si estan todas disponibles
     * @param fichero
     * @return
     */
    @Override
    public boolean esPosibleDescargar(Fichero fichero) {
        //TODO Refactorizar para recuperar por hilos de descarga.

        List<MapEntryPhoto> partes = this.mapEntryPhotoService.findByFichero(fichero);
        logger.info("Recuperando URL de {} partes",fichero.getPartes());

//        for (MapEntryPhoto entry : partes) {
//            PrinterUtil.printParte(entry.getParte(),partes.size());
//            if (!mapEntryPhotoService.esPosibleDescargar(entry))
//                return false;
//        }
        List<MapEntryPhoto> elResultado = requestUriManager.recoverUris(partes);
        if(elResultado.size()!=partes.size())return false;
        return true;
    }

    @Override
    public List<Fichero> findAllAloneFichero() {
        return this.dao.findAllAloneFichero();
    }

    @Override
    public Fichero findById(Long id) {
        return this.dao.findById(id).get();
    }


    private long getKB(Fichero fichero) {
        return fichero.getFile().length() / 1024;
    }

    private double getMB(Fichero fichero) {
        return (double) fichero.getFile().length() / 1024 / 1024;
    }

    public boolean isVideo(Fichero fichero) {
        return getTipo(fichero).equals("video");
    }

    private boolean isImage(Fichero fichero) {
        return getTipo(fichero).equals("image");
    }

    private boolean isAudio(Fichero fichero) {
        return getTipo(fichero).equals("audio");
    }

    private boolean isUnknown(Fichero fichero) {
        return getTipo(fichero).equals("unknown");
    }


    private String getTipo(Fichero fichero) {
        if (fichero.getFile().isDirectory())
            return "dir";
        String[] formato = fichero.getFile().getName().split("\\.");
        String extension = formato[formato.length - 1];
        switch (extension.toLowerCase()) {
            case "mov":
            case "avi":
            case "mpg":
            case "mkv":
            case "mpeg":
            case "mp4":
                return "video";
            case "jpg":
            case "png":
            case "jpeg":
            case "bmp":
                return "image";
            case "mp3":
            case "wav":
            case "flac":
                return "audio";
            default:
                return "unknown";

        }
    }

}
