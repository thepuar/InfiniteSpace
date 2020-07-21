package es.thepuar.InfiniteSpace.controller;

import es.thepuar.InfiniteSpace.manager.ResourceManager;
import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.FicheroDirectorio;
import es.thepuar.InfiniteSpace.service.api.FicheroService;
import es.thepuar.InfiniteSpace.utils.Ruta;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public abstract class DirectorioController {

    protected String rutaNavegacion = "";

    @Autowired
    protected Ruta ruta;

    @Autowired
    protected FicheroService ficheroService;

    protected List<Fichero> ficheros;

    protected abstract void ponRuta();

    @PostConstruct
    protected void iniciliza() {
        ficheros = ficheroService.findAll();
        ponRuta();
    }

    public String getRuta() {
        return this.ruta.getRuta();
    }

    public File getRutaFile() {
        return this.ruta.getFile();
    }

    public void setRuta(String ruta) {
        this.ruta.setRuta(ruta);
    }

    @GetMapping("changedir")
    public String abreDirectorio(@RequestParam("directory") Integer id) {
        File directorio = new File(ruta.getRuta());
        if (id == 0) {
            this.ruta.setFile(this.ruta.getFile().getParentFile());
        } else {
            File nuevaRuta = Arrays.asList(directorio.listFiles()).get(id - 1);
            ruta.setRuta(nuevaRuta.getAbsolutePath());
        }
        if (!rutaNavegacion.isEmpty())
            return "redirect:" + rutaNavegacion + "/create";
        else
            return "redirect:/";
    }

    public void cargaFicherosDeLaRuta(ModelAndView mav) {
        if (StringUtils.isBlank(this.getRuta()))
            this.setRuta(ResourceManager.getProperty("ruta_upload"));
        File directorio = new File(this.getRuta());

        List<FicheroDirectorio> ficheroDirectorios = new ArrayList<>();

        if (directorio.isDirectory()) {
            if(this.getRutaFile().getParentFile()!=null)
                ficheroDirectorios.add(new FicheroDirectorio(0, this.getRutaFile().getParentFile(), ".."));
            int i = 1;
            for (File file : directorio.listFiles()) {
                ficheroDirectorios.add(new FicheroDirectorio(i++, file));

            }

        }

        mav.addObject("files", ficheroDirectorios);
        mav.addObject("navegacion", rutaNavegacion);
    }
}
