package es.thepuar.InfiniteSpace.manager;

import es.thepuar.InfiniteSpace.google.client.PhotoClientJava;
import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.Pelicula;
import es.thepuar.InfiniteSpace.model.Referencia;
import es.thepuar.InfiniteSpace.service.api.FicheroService;
import es.thepuar.InfiniteSpace.service.api.FileToPng;
import es.thepuar.InfiniteSpace.service.api.MapEntryPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class UploadManager implements Observer {

    private final int UPLOAD_MANAGERS = 1;

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

    List<File> filesToUpload;
    int indexFilesToUpload;

    public UploadManager() {
        // inicializar();
    }

    /**
     * Metodo para subir ficheros
     *
     * @param files
     */
    public void uploadFicheros(List<File> files) {
        inicializar();
        this.filesToUpload = files;
        for (int i = 0; i < this.UPLOAD_MANAGERS; i++) {
            UploadWorkerManager manager = findFree();
            if (manager != null) {
                if (isMoreWork())
                    uploadFichero(getNextFile(), manager);
                else
                    System.out.println("No hay más ficheros para subir");
            }
        }
    }

    public void uploadPelicula(Pelicula pelicula){
        inicializar();
        this.filesToUpload = Arrays.asList(pelicula.getFichero().getFile());
        for (int i = 0; i < this.UPLOAD_MANAGERS; i++) {
            UploadWorkerManager manager = findFree();
            if (manager != null) {
                if (isMoreWork())
                    uploadPelicula(pelicula, manager);
                else
                    System.out.println("No hay más ficheros para subir");
            }
        }
    }

    public void uploadDirectorio(File file){
        System.out.println("Subiendo directorio con "+file.list().length+" ficheros");
        List<String> ficheros = null;
        ficheros =Arrays.asList(file.list());
        List<File> files = ficheros.stream().map(cadena -> new File(file.getAbsolutePath()+"\\"+cadena)).collect(Collectors.toList());
        uploadFicheros(files);
    }

    private void uploadPelicula(Pelicula pelicula, UploadWorkerManager manager){
        this.indexFilesToUpload++;
        Fichero fichero = pelicula.getFichero();
        System.out.println("Subiendo Pelicula "+fichero.getNombreYExtension());
        List<Referencia> referencias = this.fileService.convertFichero2Png(fichero);
        this.ficheroService.save(fichero);
        System.out.println("\nSubiendo partes del fichero");
        manager.uploadReferencias(referencias);
    }

    private void uploadFichero(File file, UploadWorkerManager manager) {
        Fichero fichero = ficheroService.fileToFichero(file);
        System.out.println("Subiendo fichero "+fichero.getNombreYExtension());
        List<Referencia> referencias = this.fileService.convertFichero2Png(fichero);
        this.ficheroService.save(fichero);
        System.out.println("\nSubiendo partes del fichero");
        manager.uploadReferencias(referencias);
        //managers.add(new UploadWorkerManager(context, referencias,this));
        //this.photoService.uploadFiles(referencias);
        //deleteFiles(referencias);
    }

    private void inicializar() {
        this.indexFilesToUpload = 0;
        this.filesToUpload = new ArrayList<>();
        this.managers = new ArrayList<>();
        for (int i = 0; i < UPLOAD_MANAGERS; i++) {
            managers.add(new UploadWorkerManager(context, this));
        }
    }


    @Override
    public void update(Observable o, Object value) {
        List<Referencia> referenciasTerminadas = (List<Referencia>) value;
        for (Referencia referencia : referenciasTerminadas) {
            this.mapEntryPhotoService.save(referencia.getEntry());
        }
        System.out.println("\nFichero Subido " + referenciasTerminadas.get(0).getEntry().getFichero().getNombreYExtension());
        System.out.println("Eliminando imagenes temporales");
        this.deleteFiles(referenciasTerminadas);

        //Buscar si hay más ficheros para subir
        if (isMoreWork()) {
            //Pasar más trabajo
            System.out.println("Asignando más trabajo al UploadManagerWorker");
            uploadFichero(getNextFile(), findFree());
        }else{
            System.out.println("No hay más ficheros, se han subido todos.");
        }

    }

    private void deleteFiles(List<Referencia> referencias) {
        for (Referencia referencia : referencias) {
            this.mapEntryPhotoService.save(referencia.getEntry());
            File f = new File(referencia.getRuta());
            f.delete();
        }
    }

    /**
     * Encuentra un Manager Libre
     *
     * @return
     */
    private UploadWorkerManager findFree() {
        for (UploadWorkerManager manager : this.managers) {
            if (!manager.isTrabajando())
                return manager;
        }
        return null;
    }

    /**
     * Indica si hay más ficheros para subir
     *
     * @return
     */
    private boolean isMoreWork() {
        if (this.filesToUpload.size()  == indexFilesToUpload) return false;
        else return true;
    }

    /**
     * Devuelve el siguiente fichero a subir
     *
     * @return
     */
    private File getNextFile() {
        if (this.filesToUpload.get(indexFilesToUpload) != null)
            return this.filesToUpload.get(indexFilesToUpload++);
        return null;
    }
}
