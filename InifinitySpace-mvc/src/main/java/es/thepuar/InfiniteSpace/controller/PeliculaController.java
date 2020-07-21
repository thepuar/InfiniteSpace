package es.thepuar.InfiniteSpace.controller;

import es.thepuar.InfiniteSpace.manager.DownloadManager;
import es.thepuar.InfiniteSpace.manager.UploadManager;
import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.Pelicula;
import es.thepuar.InfiniteSpace.model.PeliculaForm;
import es.thepuar.InfiniteSpace.service.api.FicheroService;
import es.thepuar.InfiniteSpace.service.api.PeliculaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/pelicula")
public class PeliculaController extends DirectorioController {

    @Autowired
    PeliculaService peliculaService;

    @Autowired
    FicheroService ficheroService;

    @Autowired
    UploadManager uploadManager;

    @Autowired
    DownloadManager downloadManager;


    protected void ponRuta(){
        this.rutaNavegacion ="/pelicula";
    }

    @GetMapping("")
    public ModelAndView list() {
        List<Pelicula> peliculas = this.peliculaService.findAll();
        ModelAndView mav = new ModelAndView("peliculas");
        mav.addObject("peliculas", peliculas);
        return mav;
    }

    @GetMapping("{id}")
    public ModelAndView detail(@PathVariable("id") Long id) {
        return null;
    }


    @GetMapping("create")
    public ModelAndView createMovie() {
        ModelAndView mav = new ModelAndView("form/addPelicula");
        List<Fichero> ficheroSueltos = this.ficheroService.findAllAloneFichero();
        this.cargaFicherosDeLaRuta(mav);
        mav.addObject("peliculaForm", new PeliculaForm());
        return mav;
    }

    @PostMapping("add")
    public ModelAndView addPelicula(@ModelAttribute PeliculaForm peliculaForm, BindingResult bindingResult) {
        ModelAndView mav = new ModelAndView("redirect:/pelicula");
        Fichero fichero = new Fichero();
        fichero.setFile(peliculaForm.getFicheroDirectorio().getFile());
        ficheroService.completeFichero(fichero);
        peliculaForm.getPelicula().setFichero(fichero);
        peliculaService.completePelicula(peliculaForm.getPelicula());

        if(StringUtils.isBlank(peliculaForm.getPelicula().getNombre())){
            peliculaForm.getPelicula().setNombre(peliculaForm.getFicheroDirectorio().getFile().getName());
        }
        this.ficheroService.save(fichero);
        this.peliculaService.save(peliculaForm.getPelicula());

        this.uploadManager.uploadPelicula(peliculaForm.getPelicula());

        return mav;
    }

    @GetMapping("delete/{id}")
    public ModelAndView deletePelicula(@PathVariable("id") Long id) {

        this.peliculaService.delete(this.peliculaService.findById(id));
        return new ModelAndView("redirect:/pelicula");
    }

    @GetMapping("download/{id}")
    public ModelAndView downloadPelicula(@PathVariable("id") Long id) {
        Pelicula pelicula = peliculaService.findById(id);
        downloadManager.downloadFichero(pelicula.getFichero());

        return new ModelAndView("redirect:/pelicula");
    }
}
