package es.thepuar.InfiniteSpace.workers;

import es.thepuar.InfiniteSpace.exception.DownloadException;
import es.thepuar.InfiniteSpace.manager.DownloadWorkerManager;
import es.thepuar.InfiniteSpace.manager.ResourceManager;

import es.thepuar.InfiniteSpace.model.MapEntryPhoto;
import es.thepuar.InfiniteSpace.model.Referencia;
import es.thepuar.InfiniteSpace.utils.PrinterUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

@Service
public class DownloadWorker extends Observable implements Runnable {
    private static final Logger logger = LogManager.getLogger(DownloadWorker.class);

    private Boolean running;
    private MapEntryPhoto parte;
    private String nombreHilo;

    public DownloadWorker() {
        ;
    }

    public String getNombreHilo() {
        return this.nombreHilo;
    }

    public DownloadWorker(String nombreHilo, DownloadWorkerManager manager) {
        running = true;
        this.nombreHilo = nombreHilo;
        this.addObserver(manager);
        // this.parte = parte;
    }

    public void addMapEntryPhoto(MapEntryPhoto mapEntryPhoto) {
        this.parte = mapEntryPhoto;

    }

    public void descarga(MapEntryPhoto parte) throws DownloadException {
        String rutaFichero = ResourceManager.getProperty("ruta_descarga") + "\\" + this.nombreHilo + "_" + parte.getFichero().getNombre() + "_" + Calendar.getInstance().getTimeInMillis() + ".png";
        try {
            this.downloadFromUrl(parte.getUrl1920(), rutaFichero);
        } catch (Exception e) {
            logger.error("No se ha podido descargar la parte {} - {}", parte.getParte(), e.getLocalizedMessage());
        }
        Referencia referencia = new Referencia(rutaFichero, parte);
        setChanged();
        notifyObservers(referencia);

    }

    public void parar(){
        this.running = false;
        System.out.println(" "+this.getNombreHilo());
    }

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
    }

    @Override
    public void run() {
        while (running) {
            if (this.parte == null) {
             //   System.out.println(this.nombreHilo + "Nada a descargar, voy a dormir");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    System.out.println(" "+this.getNombreHilo());
                    Thread.currentThread().interrupt();

                }
            } else {

                logger.info("{} Descargando parte {}", this.nombreHilo, parte.getParte());
                try {
                    this.descarga(this.parte);
                } catch (DownloadException e) {

                    logger.error(" {} / {} del fichero {}", this.parte.getParte(), this.parte.getFichero().getPartes(), this.parte.getFichero().getNombre());
                    this.parte = null;
                }
            }
        }

    }

    public void downloadFromUrl(String url, String file) throws IOException {

        try {
            BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            in.close();
            fileOutputStream.close();
        } catch (Exception e) {
            // handle exception
            e.printStackTrace();

            throw new IOException("Error descargando parte "+e.getLocalizedMessage());
        }

    }
}
