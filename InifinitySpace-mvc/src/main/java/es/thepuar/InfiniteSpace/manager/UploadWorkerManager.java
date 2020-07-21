package es.thepuar.InfiniteSpace.manager;

import es.thepuar.InfiniteSpace.google.client.PhotoClientJava;
import es.thepuar.InfiniteSpace.model.Referencia;
import es.thepuar.InfiniteSpace.workers.UploadWorker;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class UploadWorkerManager extends Observable implements Observer {

    private boolean trabajando = false;

    private int MAX_HILOS = 1;
    private Thread[] hilos = new Thread[MAX_HILOS];
    private UploadWorker[] trabajadores = new UploadWorker[MAX_HILOS];
    List<Referencia> referenciasTerminadas;
    List<Referencia> referencias;
    private ApplicationContext context;
    private PhotoClientJava photoClient;
    private UploadManager uploadManager;

    public UploadWorkerManager(ApplicationContext context, UploadManager manager){
        this.trabajando = false;
        this.uploadManager = manager;
        this.context = context;
        this.referencias = new ArrayList<>();
        this.referenciasTerminadas = new ArrayList<>();
        this.addObserver(manager);
    }

    public void uploadReferencias(List<Referencia> referencias){
        this.referenciasTerminadas = new ArrayList<>();
        this.trabajando = true;
        this.referencias = referencias;
        for (int i = 0; i < MAX_HILOS; i++) {
            trabajadores[i] = new UploadWorker("HILO_UP_" + i, this.getSubList(referencias, i),this);
            hilos[i] = new Thread(trabajadores[i]);
            context.getAutowireCapableBeanFactory().autowireBean(trabajadores[i]);
            hilos[i].start();
        }
    }

    public UploadWorkerManager(ApplicationContext context, List<Referencia> referencias, UploadManager manager) {
        this.trabajando = true;
        this.uploadManager = manager;
        this.context = context;
        this.referencias = referencias;
        referenciasTerminadas = new ArrayList<>();
        this.addObserver(manager);
        for (int i = 0; i < MAX_HILOS; i++) {
            trabajadores[i] = new UploadWorker("HILO_UP_" + i, this.getSubList(referencias, i),this);
            hilos[i] = new Thread(trabajadores[i]);
            context.getAutowireCapableBeanFactory().autowireBean(trabajadores[i]);
            hilos[i].start();
        }
    }



    @Override
    public void update(Observable o, Object value) {
        List<Referencia> referencias = (List<Referencia>) value;
        this.referenciasTerminadas.addAll(referencias);
        if(referenciasTerminadas.size()== this.referencias.size()){
           //Ha terminado, no hace falta ordenar
            this.trabajando = false;
            setChanged();
            notifyObservers(this.referenciasTerminadas);

        }
    }

    private List<Referencia> getSubList(List<Referencia> referencias, int num_hilo) {
        List<Referencia> result = new ArrayList<>();
        int cuentaHilos = 0;
        for (int i = 0; i < referencias.size(); i++) {
            if (cuentaHilos == num_hilo)
                result.add(referencias.get(i));
            if (cuentaHilos == MAX_HILOS - 1)
                cuentaHilos = 0;
            else
                cuentaHilos++;
        }
        return result;
    }

    public boolean isTrabajando() {
        return trabajando;
    }

    public void setTrabajando(boolean trabajando) {
        this.trabajando = trabajando;
    }
}
