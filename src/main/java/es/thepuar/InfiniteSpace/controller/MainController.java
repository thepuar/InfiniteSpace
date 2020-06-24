package es.thepuar.InfiniteSpace.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.List;

import es.thepuar.InfiniteSpace.google.client.PhotoClientJava;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.rpc.ApiException;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.UserCredentials;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.PhotosLibrarySettings;
import com.google.photos.library.v1.proto.BatchCreateMediaItemsResponse;
import com.google.photos.library.v1.proto.NewMediaItem;
import com.google.photos.library.v1.proto.NewMediaItemResult;
import com.google.photos.library.v1.upload.UploadMediaItemRequest;
import com.google.photos.library.v1.upload.UploadMediaItemResponse;
import com.google.photos.library.v1.upload.UploadMediaItemResponse.Error;
import com.google.photos.library.v1.util.NewMediaItemFactory;
import com.google.photos.types.proto.Album;
import com.google.photos.types.proto.MediaItem;
import com.google.rpc.Code;
import com.google.rpc.Status;

import es.thepuar.InfiniteSpace.google.rest.PhotoRestClient;
import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.service.api.FicheroService;
import es.thepuar.InfiniteSpace.service.api.FileToPng;

@Controller
public class MainController {

	private static final java.io.File DATA_STORE_DIR = new java.io.File("H:\\Documentos\\InfiniteSpace\\zhola.png");
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final int LOCAL_RECEIVER_PORT = 61984;

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
		
		return mav;
		
	}

	
	@PostMapping("action")
	public String accion() {
		System.out.println("Has pulsado accion");
		converter.test();
		return "index.html";
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
	public String descarga(){
		this.clienteJava.downloadImage();
		return "index.html";
	}

	
	
	
	

}
