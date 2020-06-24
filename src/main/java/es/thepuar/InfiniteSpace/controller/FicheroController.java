package es.thepuar.InfiniteSpace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.service.api.FicheroService;

@Controller
@RequestMapping("/fichero")
public class FicheroController {

	@Autowired
	FicheroService ficheroService;

	@PostMapping("/add")
	public ModelAndView addFile(@ModelAttribute Fichero fichero) {
		this.ficheroService.completeFichero(fichero);
		this.ficheroService.save(fichero);
		return new ModelAndView("redirect:/");
	}
	
	
	@GetMapping("delete/{id}")
	public ModelAndView removeFile(@PathVariable("id")Long id) {
		
		return new ModelAndView("redirect:/");
	}
	
	@GetMapping("download/{id}")
	public ModelAndView downloadFile(@PathVariable("id")String id) {
		return new ModelAndView("redirect:/");
	}

}
