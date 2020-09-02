package es.thepuar.InfiniteSpace.manager;

import es.thepuar.InfiniteSpace.google.client.PhotoClientJava;
import es.thepuar.InfiniteSpace.model.MapEntryPhoto;
import es.thepuar.InfiniteSpace.utils.PrinterUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

public class RequestUriTask implements Callable {
    private static final Logger logger = LogManager.getLogger(RequestUriTask.class);

    MapEntryPhoto mapEntryPhoto = null;
    PhotoClientJava photoClientJava = null;

    int contador = 0;

    public RequestUriTask(MapEntryPhoto mapEntry, PhotoClientJava cliente) {
        this.mapEntryPhoto = mapEntry;
        this.photoClientJava = cliente;
        this.contador = contador;

    }


    @Override
    public MapEntryPhoto call()  {
        if (this.mapEntryPhoto != null) {

            this.mapEntryPhoto.setUrl(this.photoClientJava.getBaseUrl(this.mapEntryPhoto));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            PrinterUtil.printParte(this.mapEntryPhoto.getParte(), this.mapEntryPhoto.getFichero().getPartes());
            return this.mapEntryPhoto;
        }
        return null;
    }
}
