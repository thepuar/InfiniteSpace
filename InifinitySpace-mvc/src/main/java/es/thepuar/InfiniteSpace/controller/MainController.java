package es.thepuar.InfiniteSpace.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.thepuar.InfiniteSpace.manager.ResourceManager;
import es.thepuar.InfiniteSpace.google.client.PhotoClientJava;
import es.thepuar.InfiniteSpace.model.CambioRutaForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import es.thepuar.InfiniteSpace.google.rest.PhotoRestClient;
import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.FicheroDirectorio;
import es.thepuar.InfiniteSpace.service.api.FicheroService;
import es.thepuar.InfiniteSpace.service.api.FileToPng;

@Controller

public class MainController {

	private static final java.io.File DATA_STORE_DIR = new java.io.File("H:\\Documentos\\InfiniteSpace\\zhola.png");
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final int LOCAL_RECEIVER_PORT = 61984;

	@Autowired
	private Ruta ruta;

	public String getRuta(){
		return this.ruta.getRuta();
	}


	private String token = "";

	@Autowired
	FicheroService ficheroService;

	@Autowired
	FileToPng converter;



	@Autowired
	PhotoRestClient photoRestClient;

	@Autowired
	PhotoClientJava clienteJava;

	private List<Fichero> ficheros;

	@GetMapping("")
	public ModelAndView inicio() {
		ModelAndView mav = new ModelAndView("index");
		mav.addObject("fichero", new Fichero());
		ficheros = ficheroService.findAll();
		mav.addObject("ficheros", ficheros);
		if(StringUtils.isBlank(ruta.getRuta()))
			ruta.setRuta(ResourceManager.getProperty("ruta_upload"));
		File directorio = new File(ruta.getRuta());
		
		List<FicheroDirectorio> ficheroDirectorios = new ArrayList<>();

		if (directorio.isDirectory()) {
			int i = 1;
			for(File file : directorio.listFiles()) {
				ficheroDirectorios.add(new FicheroDirectorio(i++,file));
				
			}
			mav.addObject("files",ficheroDirectorios);
			mav.addObject("form",new CambioRutaForm());
		}



		return mav;

	}

	@PostMapping("ruta")
	public String accion( String nuevaRuta) {
		if(!StringUtils.isBlank(nuevaRuta)){
			File f = new File(nuevaRuta);
			if(f.isDirectory())
				this.ruta.setRuta(nuevaRuta);
		}


		return "redirect:/";
	}

	@PostMapping("path")
	public String accion(@ModelAttribute(value="form") CambioRutaForm form) {
	System.out.println("Ruta ->"+form.getRuta());
	this.ruta.setRuta(form.getRuta());
		return "redirect:/";
	}


	@PostMapping("convert")
	public String convert() {
		converter.convertFile2Png();
		return "index.html";
	}

	@GetMapping("inicio")
	public String iniciar() {
		photoRestClient.sendPost();
		return "index.html";
	}

	@PostMapping("upload")
	public String funciona() {
		System.out.println("Iniciando upload");
		this.clienteJava.uploadFile();

		return "index.html";

	}

	@PostMapping("/download")
	public String descarga() {
		this.clienteJava.downloadImage();
		return "index.html";
	}

	@GetMapping("dashboard")
	public ModelAndView dashBoard(){
		return new ModelAndView("dashboard");
	}

}
