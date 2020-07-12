package es.thepuar.InfiniteSpace.controller;

import es.thepuar.InfiniteSpace.model.Episodio;
import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.Pelicula;
import es.thepuar.InfiniteSpace.service.api.EpisodioService;
import es.thepuar.InfiniteSpace.service.api.FicheroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/episodio")
public class EpisodioController {

    @Autowired
    EpisodioService episodioService;

    @Autowired
    FicheroService ficheroService;

    @GetMapping("")
    public ModelAndView list(){
        List<Episodio> episodios =  episodioService.findAll();
        ModelAndView mav = new ModelAndView("episodios");
        mav.addObject("episodios",episodios);
        return mav;
    }

    @GetMapping("create")
    public ModelAndView createMovie() {
        ModelAndView mav = new ModelAndView("form/addEpisodio");
        List<Fichero> ficheroSueltos = this.ficheroService.findAllAloneFichero();
        mav.addObject("ficheros",ficheroSueltos);
        mav.addObject("episodio",new Episodio());
        return mav;
    }

    @PostMapping("add")
    public ModelAndView addPelicula(@ModelAttribute Episodio episodio, BindingResult bindingResult){
        ModelAndView mav = new ModelAndView("redirect:/episodio");
        this.episodioService.save(episodio);
        return mav;
    }

    @GetMapping("delete/{id}")
    public ModelAndView deleteEpisodio(@PathVariable("id") Long id) {

        this.episodioService.delete(this.episodioService.findById(id));
        return new ModelAndView("redirect:/episodio");
    }

}
