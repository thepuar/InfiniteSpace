package es.thepuar.InfiniteSpace.workers;

import es.thepuar.InfiniteSpace.google.client.PhotoClientJava;
import es.thepuar.InfiniteSpace.manager.UploadWorkerManager;
import es.thepuar.InfiniteSpace.model.Referencia;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class UploadWorker extends Observable implements Runnable {

    @Autowired
    PhotoClientJava photoClient;

    private List<Referencia> referencias;
    private Boolean running;
    private String nombreHilo;

    public UploadWorker(String nombreHilo, List<Referencia> referencias, UploadWorkerManager manager) {
        this.addObserver(manager);
        this.referencias = referencias;
        this.nombreHilo = nombreHilo;
    }

    @Override
    public void run() {

            photoClient.uploadFiles(referencias);
            //Las referencias ya tendran los ids
            notificar();

    }
    private void notificar(){
        setChanged();
        notifyObservers(referencias);
    }

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
    }
}
