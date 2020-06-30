package es.thepuar.InfiniteSpace.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import es.thepuar.InfiniteSpace.google.client.PhotoClientJava;
import es.thepuar.InfiniteSpace.manager.ResourceManager;
import es.thepuar.InfiniteSpace.model.CambioRutaForm;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Scope;
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
import sun.security.pkcs11.wrapper.Functions;

@Controller
@SessionAttributes("ruta")
public class MainController {

	private static final java.io.File DATA_STORE_DIR = new java.io.File("H:\\Documentos\\InfiniteSpace\\zhola.png");
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final int LOCAL_RECEIVER_PORT = 61984;

	private String ruta="";
	public String getRuta(){
		return this.getRuta();
	}


	private String token = "";

	@Autowired
	FicheroService ficheroService;

	@Autowired
	FileToPng converter;

	@Autowired
	ResourceManager resourceManager;

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

		File directorio = new File(resourceManager.getProperty("ruta_upload"));
		
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

	@PostMapping("action")
	public String accion() {
		System.out.println("Has pulsado accion");
		converter.test();
		return "index.html";
	}

	@PostMapping("path")
	public String accion(@ModelAttribute(value="form") CambioRutaForm form) {
	System.out.println("Ruta ->"+form.getRuta());
	this.ruta = form.getRuta();
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

}
