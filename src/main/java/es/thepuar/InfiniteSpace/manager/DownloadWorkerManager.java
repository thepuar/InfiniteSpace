package es.thepuar.InfiniteSpace.manager;

import es.thepuar.InfiniteSpace.model.MapEntryPhoto;
import es.thepuar.InfiniteSpace.model.Referencia;
import es.thepuar.InfiniteSpace.workers.DownloadWorker;

import java.util.*;

public class DownloadWorkerManager extends Observable implements Observer {

    private int MAX_HILOS = 10;
    private DownloadManager manager;
    private Thread[] hilos = new Thread[MAX_HILOS];
    private DownloadWorker[] trabajadores = new DownloadWorker[MAX_HILOS];
    private int parteADescargar = 0;
    Boolean terminado = false;
    Map<MapEntryPhoto, ESTADO> descargas;
    List<Referencia> referenciasTerminadas;

    public DownloadWorkerManager(List<MapEntryPhoto> entrys,DownloadManager manager) {
        this.manager = manager;
        this.addObserver(manager);
        restart(entrys);
        for (int i = 0; i < MAX_HILOS; i++) {
            trabajadores[i] = new DownloadWorker("HILO_" + i, this);
            hilos[i] = new Thread(trabajadores[i]);
            trabajadores[i].addMapEntryPhoto(this.nextToDownload());
            hilos[i].start();
        }
    }

    public void restart(List<MapEntryPhoto> entrys) {
        parteADescargar = 0;
        descargas = new HashMap<>();
        referenciasTerminadas = new ArrayList<>();
        for (MapEntryPhoto entry : entrys) {
            descargas.put(entry, ESTADO.SIN_DESCARGAR);
        }
        this.terminado = false;
        //Dar trabajo a cada hijo


    }

    @Override
    public void update(Observable o, Object value) {
        Referencia referencia = (Referencia) value;
        //Marcar parte como descargada
        descargas.put(referencia.getEntry(), ESTADO.DESCARGADO);
        DownloadWorker dww = (DownloadWorker) o;
        dww.addMapEntryPhoto(null);
        referenciasTerminadas.add(referencia);
        checkTerminado();
        if (!terminado) {
            //No ha terminado, asignar otra parte a este hilo
            System.out.println("Asignando otra descarga a " + dww.getNombreHilo());
            dww.addMapEntryPhoto(nextToDownload());
        } else {
            System.out.println("He descargado todas las partes --> Parando los hilos");
            for (int i = 0; i < MAX_HILOS; i++) {
                trabajadores[i].parar();

            }
            //Notificando que se ha terminado con la descarga del fichero
            setChanged();
            notifyObservers(referenciasTerminadas);
        }
    }


    /**
     * Comprueba si se han terminado de descargar todas las partes
     */
    public void checkTerminado() {
        Boolean terminado = true;
        for (Map.Entry<MapEntryPhoto, ESTADO> entradas : descargas.entrySet()) {
            if (!entradas.getValue().equals(ESTADO.DESCARGADO))
                terminado = false;
        }
        this.terminado = terminado;

    }

    public MapEntryPhoto nextToDownload() {
        for (Map.Entry<MapEntryPhoto, ESTADO> entrada : descargas.entrySet()) {
            if (entrada.getValue().equals(ESTADO.SIN_DESCARGAR)) {
                entrada.setValue(ESTADO.DESCARGANDO);
                return entrada.getKey();
            }
        }
        return null;
    }

    public enum ESTADO {
        SIN_DESCARGAR,
        DESCARGANDO,
        DESCARGADO
    }


}
