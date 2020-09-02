package es.thepuar.InfiniteSpace.manager;

import es.thepuar.InfiniteSpace.google.client.PhotoClientJava;
import es.thepuar.InfiniteSpace.model.MapEntryPhoto;
import es.thepuar.InfiniteSpace.service.api.MapEntryPhotoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

@Service
public class RequestUriManager {
    private static final Logger logger = LogManager.getLogger(RequestUriManager.class);

    @Autowired
    PhotoClientJava cliente;

    @Autowired
    MapEntryPhotoService mapEntryPhotoService;

    int numHilos = 5;
    ExecutorService executor = null;


    public RequestUriManager(){
         executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numHilos);

    }

    public List<MapEntryPhoto> recoverUris(List<MapEntryPhoto> lista){
        List<MapEntryPhoto> result  = new ArrayList<>();

        logger.debug("Descargardo URIS");
        Iterator<MapEntryPhoto> iterator = lista.iterator();
        int contador = 0;
        List<Callable<MapEntryPhoto>> listaTareas = new ArrayList<>();
        while(iterator.hasNext()) {
            MapEntryPhoto mapEntryPhoto = iterator.next();
            //executor.execute(new RequestUriTask(mapEntryPhoto,cliente, result));
            listaTareas.add( new RequestUriTask(mapEntryPhoto,cliente));
        }
            //Future<MapEntryPhoto> future = (Future<MapEntryPhoto>) executor.submit(new RequestUriTask(mapEntryPhoto,cliente));
        try {
            List<Future<MapEntryPhoto>> listaFuturos = executor.invokeAll(listaTareas);
            for(Future<MapEntryPhoto> futuro: listaFuturos){
                result.add(futuro.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

       /* while(future.isDone() == false) {
                try {
                    result.add(future.get());
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                //Sleep for 1 second
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/



        logger.debug("Todas las urls obtenidas");



        for(MapEntryPhoto mapEntryPhoto : lista){
            this.mapEntryPhotoService.save(mapEntryPhoto);
        }
        return lista;
    }
}
