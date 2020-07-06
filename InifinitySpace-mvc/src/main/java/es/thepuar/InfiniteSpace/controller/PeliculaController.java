package es.thepuar.InfiniteSpace.controller;

import es.thepuar.InfiniteSpace.service.api.PeliculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/pelicula")
public class PeliculaController {

    @Autowired
    PeliculaService peliculaService;

    @GetMapping("{id}")
    public ModelAndView detail(@PathVariable("id") Long id) {
        return null;
    }


    @GetMapping("create")
    public ModelAndView createMovie() {
        ModelAndView mav = new ModelAndView("form/addPelicula");
        return mav;
    }
}
