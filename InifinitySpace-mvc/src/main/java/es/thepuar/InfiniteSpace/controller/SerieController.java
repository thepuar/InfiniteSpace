package es.thepuar.InfiniteSpace.controller;

import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.Pelicula;
import es.thepuar.InfiniteSpace.model.Serie;
import es.thepuar.InfiniteSpace.service.api.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/serie")
public class SerieController {

    @Autowired
    SerieService serieService;

    @GetMapping("")
    public ModelAndView list(){
        List<Serie> series = this.serieService.findAll();
        ModelAndView mav = new ModelAndView("series");
        mav.addObject("series",series);
        return mav;
    }

    @GetMapping("{id}")
    public ModelAndView detail(@PathVariable("id")Long id){return null;}

    /**
     * Formulario para crear una serie
     * @return
     */
    @GetMapping("create")
    public ModelAndView createMovie() {
        ModelAndView mav = new ModelAndView("form/addSerie");
        //List<Fichero> ficheroSueltos = this.ficheroService.findAllAloneFichero();
        //mav.addObject("ficheros",ficheroSueltos);
        mav.addObject("serie",new Serie());
        return mav;
    }

    /**
     * Captura el formulario y crea una serie
     * @param serie
     * @param bindingResult
     * @return
     */
    @PostMapping("add")
    public ModelAndView addSerie(@ModelAttribute Serie serie, BindingResult bindingResult){
        ModelAndView mav = new ModelAndView("redirect:/serie");
        this.serieService.save(serie);
        return mav;
    }

    /**
     * Elimina una serie
     * @param id
     * @return
     */
    @GetMapping("delete/{id}")
    public ModelAndView deleteSerie(@PathVariable("id") Long id) {

        this.serieService.delete(this.serieService.findById(id));
        return new ModelAndView("redirect:/");
    }
}
