package es.thepuar.InfiniteSpace.manager;

import es.thepuar.InfiniteSpace.google.client.PhotoClientJava;
import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.Referencia;
import es.thepuar.InfiniteSpace.service.api.FicheroService;
import es.thepuar.InfiniteSpace.service.api.FileToPng;
import es.thepuar.InfiniteSpace.service.api.MapEntryPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

@Component
public class UploadManager implements Observer {

    @Autowired
    MapEntryPhotoService mapEntryPhotoService;

    @Autowired
    FicheroService ficheroService;

    @Autowired
    FileToPng fileService;

    @Autowired
    PhotoClientJava photoService;

    @Autowired
    ApplicationContext context;

    List<UploadWorkerManager> managers;

    public UploadManager() {
        this.managers = new ArrayList<>();

    }

    public void uploadFichero(File file) {
        Fichero fichero = ficheroService.fileToFichero(file);
        List<Referencia> referencias = this.fileService.convertFichero2Png(fichero);
        this.ficheroService.save(fichero);
        managers.add(new UploadWorkerManager(context, referencias,this));
        //this.photoService.uploadFiles(referencias);
      //  deleteFiles(referencias);
    }

    @Override
    public void update(Observable o, Object value) {
        List<Referencia> referenciasTerminadas = (List<Referencia>) value;
        for (Referencia referencia : referenciasTerminadas) {
            this.mapEntryPhotoService.save(referencia.getEntry());
        }
        System.out.println("Fichero Subido "+referenciasTerminadas.get(0).getEntry().getFichero().getNombreYExtenxion() );
        System.out.println("Eliminando imagenes temporales");
        this.deleteFiles(referenciasTerminadas);
    }

    private void deleteFiles(List<Referencia> referencias) {
        for (Referencia referencia : referencias) {
            this.mapEntryPhotoService.save(referencia.getEntry());
            File f = new File(referencia.getRuta());
            f.delete();
        }
    }
}
