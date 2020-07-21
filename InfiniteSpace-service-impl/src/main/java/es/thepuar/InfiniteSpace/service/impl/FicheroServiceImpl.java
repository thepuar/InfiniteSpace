package es.thepuar.InfiniteSpace.service.impl;

import es.thepuar.InfiniteSpace.dao.FicheroDAO;
import es.thepuar.InfiniteSpace.google.client.PhotoClientJava;
import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.MapEntryPhoto;
import es.thepuar.InfiniteSpace.model.Referencia;
import es.thepuar.InfiniteSpace.service.api.FicheroService;
import es.thepuar.InfiniteSpace.service.api.FileToPng;
import es.thepuar.InfiniteSpace.service.api.MapEntryPhotoService;
import es.thepuar.InfiniteSpace.utils.PrinterUtil;
import es.thepuar.InfiniteSpace.utils.XugglerUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Calendar;
import java.util.List;

@Service
public class FicheroServiceImpl implements FicheroService {

    @Autowired
    FicheroDAO dao;

    @Autowired
    MapEntryPhotoService mapEntryPhotoService;

    @Autowired
    PhotoClientJava photoClient;

    @Autowired
    FileToPng converter;

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


    private String getNombreFileName(String fichero) {
        String[] tokens = fichero.split("\\.");
        return tokens[0];
    }

    private String getExtensionFileName(String fichero) {
        return fichero.split("\\.")[1];
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
        this.converter.createOriginalFromReferencia(referencias);


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
        List<Referencia> referencias = converter.convertFichero2Png(fichero);
        System.out.println("Partes creadas");
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
        this.converter.compareFiles("Z:\\App\\InfiniteSpace\\upload\\trincherasruthmalena.PNG", "Z:\\App\\InfiniteSpace\\final\\trincherasruthmalena.PNG");
    }

    @Override
    public boolean esPosibleDescargar(Fichero fichero) {
        boolean resultado = true;
        List<MapEntryPhoto> partes = this.mapEntryPhotoService.findByFichero(fichero);
        System.out.println("Recuperando URL de " + fichero.getPartes() + " partes");
        for (MapEntryPhoto entry : partes) {
            PrinterUtil.printParte(entry.getParte());
            if (!mapEntryPhotoService.esPosibleDescargar(entry))
                return false;
        }
        System.out.println();
        return resultado;
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
