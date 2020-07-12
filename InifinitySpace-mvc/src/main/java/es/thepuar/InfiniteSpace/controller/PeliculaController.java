package es.thepuar.InfiniteSpace.controller;

import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.Pelicula;
import es.thepuar.InfiniteSpace.service.api.FicheroService;
import es.thepuar.InfiniteSpace.service.api.PeliculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/pelicula")
public class PeliculaController {

    @Autowired
    PeliculaService peliculaService;

    @Autowired
    FicheroService ficheroService;

    @GetMapping("")
    public ModelAndView list(){
        List<Pelicula> peliculas = this.peliculaService.findAll();
        ModelAndView mav = new ModelAndView("peliculas");
        mav.addObject("peliculas",peliculas);
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
        mav.addObject("ficheros",ficheroSueltos);
        mav.addObject("pelicula",new Pelicula());
        return mav;
    }

    @PostMapping("add")
    public ModelAndView addPelicula(@ModelAttribute Pelicula pelicula, BindingResult bindingResult){
        ModelAndView mav = new ModelAndView("redirect:/pelicula");
        this.peliculaService.save(pelicula);
        return mav;
    }

    @GetMapping("delete/{id}")
    public ModelAndView deletePelicula(@PathVariable("id") Long id) {

        this.peliculaService.delete(this.peliculaService.findById(id));
        return new ModelAndView("redirect:/pelicula");
    }
}
