package es.thepuar.InfiniteSpace.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.FicheroDirectorio;
import es.thepuar.InfiniteSpace.model.Referencia;
import es.thepuar.InfiniteSpace.service.api.FicheroService;
import es.thepuar.InfiniteSpace.service.api.FileToPng;
import es.thepuar.InfiniteSpace.service.api.MapEntryPhotoService;

@Controller
@RequestMapping("/fichero")
public class FicheroController {

	@Autowired
	FicheroService ficheroService;
	
	@Autowired
	MapEntryPhotoService mapEntryPhotoDao;

	@Autowired
	FileToPng fileService;

	@PostMapping("/add")
	public ModelAndView addFile(@ModelAttribute Fichero fichero) {
		this.ficheroService.completeFichero(fichero);
		this.ficheroService.save(fichero);
		this.ficheroService.uploadFile(fichero);

		return new ModelAndView("redirect:/");
	}

	@GetMapping("delete/{id}")
	public ModelAndView removeFile(@PathVariable("id") Long id) {
		
		this.ficheroService.delete(this.ficheroService.findById(id));
		return new ModelAndView("redirect:/");
	}

	@GetMapping("download/{id}")
	public ModelAndView downloadFile(@PathVariable("id") Long id) {
		return new ModelAndView("redirect:/");
	}

	@GetMapping("upload/{id}")
	public ModelAndView uploadFile(@PathVariable("id") Long id) {
		Fichero fichero = this.ficheroService.findById(id);
		fichero.setUploaded(true);
		this.ficheroService.save(fichero);

		return new ModelAndView("redirect:/");
	}

	@GetMapping("directorio")
	public ModelAndView getRuta() {

		return new ModelAndView("redirect:/");

	}

	@GetMapping("split/{id}")
	public ModelAndView split(@PathVariable("id") Long id) {
		File directorio = new File("Z:\\App\\InfiniteSpace\\upload");
		File toSplit = null;
		List<FicheroDirectorio> ficheroDirectorios = new ArrayList<>();

		if (directorio.isDirectory()) {
			int i = 1;
			for (File file : directorio.listFiles()) {
				if (id == i) {
					toSplit = file;
				}
				ficheroDirectorios.add(new FicheroDirectorio(i++, file));

			}
			if (toSplit != null) {
				Fichero fichero = ficheroService.fileToFichero(toSplit);
				this.fileService.convertFichero2Png(fichero);
			}

		}
		return new ModelAndView("redirect:/");
	}

	@GetMapping("splitAndStore/{id}")
	public ModelAndView splitAndStore(@PathVariable("id") Long id) {
		File directorio = new File("Z:\\App\\InfiniteSpace\\upload");
		File toSplit = null;
		List<FicheroDirectorio> ficheroDirectorios = new ArrayList<>();

		if (directorio.isDirectory()) {
			int i = 1;
			for (File file : directorio.listFiles()) {
				if (id == i) {
					toSplit = file;
				}
				ficheroDirectorios.add(new FicheroDirectorio(i++, file));

			}
			if (toSplit != null) {
				Fichero fichero = ficheroService.fileToFichero(toSplit);
				List<Referencia> referencias = this.fileService.convertFichero2Png(fichero);
				this.ficheroService.save(fichero);
				for(Referencia referencia : referencias) {
					this.mapEntryPhotoDao.save(referencia.getEntry());
				}
			}

		}
		return new ModelAndView("redirect:/");
	}

}
