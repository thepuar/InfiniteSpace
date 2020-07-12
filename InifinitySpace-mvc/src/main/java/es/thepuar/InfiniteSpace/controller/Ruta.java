package es.thepuar.InfiniteSpace.controller;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.File;

@Data
@Component
public class Ruta {
    String ruta;
    File file;

    public void setRuta(String ruta){
        this.ruta = ruta;
        this.file = new File(ruta);
    }

    public void setFile(File file){
        this.file = file;
        this.ruta = file.getAbsolutePath();
    }
}
