package es.thepuar.InfiniteSpace.manager;


import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.MapEntryPhoto;
import es.thepuar.InfiniteSpace.model.Referencia;
import es.thepuar.InfiniteSpace.service.api.FicheroService;
import es.thepuar.InfiniteSpace.service.api.FileToPng;
import es.thepuar.InfiniteSpace.service.api.MapEntryPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

@Component
public class DownloadManager implements Observer {

    @Autowired
    MapEntryPhotoService mapEntryPhotoService;

    @Autowired
    FicheroService ficheroService;

    @Autowired
    FileToPng fileToPng;

    List<DownloadWorkerManager> managers;

    public DownloadManager() {
        this.managers = new ArrayList<>();
        ;
    }

    public void downloadFichero(Fichero fichero) {
        if (this.ficheroService.esPosibleDescargar(fichero)) {
            List<MapEntryPhoto> partes = mapEntryPhotoService.findByFichero(fichero);
            Boolean anyadido = false;
            for (DownloadWorkerManager manager : managers) {
                if (manager.terminado) {
                    //Dar más trabajo
                    manager.restart(partes);
                    anyadido = true;
                }
            }
            if (!anyadido) {
                //Crear un nuevo Worker
                managers.add(new DownloadWorkerManager(partes,this));
            }

        }
    }

    public void nuevoWorker(List<MapEntryPhoto> partes) {
        this.managers.add(new DownloadWorkerManager(partes,this));
    }

    @Override
    public void update(Observable o, Object arg) {

        List<Referencia> referencias = (List<Referencia>)arg;
        System.out.println("Construyendo fichero"+referencias.get(0).getEntry().getFichero().getNombre());
        this.fileToPng.createOriginalFromReferencia(ordenarPartes(referencias));
        this.deleteTempImage(referencias);
    }

    private List<Referencia> ordenarPartes(List<Referencia> referencias){
        List<Referencia> ordenado = new ArrayList<>();
        int partes = referencias.get(0).getEntry().getFichero().getPartes();
        for(int i = 1;i<partes;i++){
            for(Referencia referencia : referencias){
                if(referencia.getEntry().getParte()==i){
                    ordenado.add(referencia);
                    break;
                }
            }
        }
        return ordenado;

    }

    private boolean deleteTempImage(List<Referencia> referencias){
        System.out.println("Eliminando ficheros temporales");
        boolean result = true;
        for(Referencia referencia : referencias){
            File f = new File(referencia.getRuta());
            if(!f.delete())
                result = false;
        }
        return result;
    }
}
