package es.thepuar.InfiniteSpace.service.impl;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.thepuar.InfiniteSpace.dao.FicheroDAO;
import es.thepuar.InfiniteSpace.google.client.PhotoClientJava;
import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.MapEntryPhoto;
import es.thepuar.InfiniteSpace.model.Referencia;
import es.thepuar.InfiniteSpace.service.api.FicheroService;
import es.thepuar.InfiniteSpace.service.api.FileToPng;
import es.thepuar.InfiniteSpace.service.api.MapEntryPhotoService;

@Service
public class FicheroServiceImpl implements FicheroService{

	@Autowired
	FicheroDAO dao;
	
	@Autowired
	MapEntryPhotoService mapEntryPhotoService;
	
	@Autowired
	PhotoClientJava photoClient;
	
	@Autowired
	FileToPng converter;
	
	@Override
	public void save(Fichero fichero) {
		dao.save(fichero);
		
	}

	@Override
	public List<Fichero> findAll() {
		return dao.findAll();
	}

	@Override
	public void completeFichero(Fichero fichero) {
		if(StringUtils.isBlank(fichero.getNombre())) {
			fichero.setNombre(this.getNombreFileName(fichero.getFile().getName()));
		}
		
		if(StringUtils.isBlank(fichero.getExtension())) {
			fichero.setExtension(this.getExtensionFileName(fichero.getFile().getName()));
		}
		fichero.setBytes(fichero.getFile().length());
		fichero.setFechaCreacion(Calendar.getInstance());
		
	}
	
	
	
	private String getNombreFileName(String fichero) {
		String[] tokens = fichero.split("\\."); 
		return tokens[0];
	}
	
	private String getExtensionFileName(String fichero) {
		return fichero.split("\\.")[1];
	}
	
	private String getNombrePath(String path) {
		String[] tokens = StringUtils.split("\\");
		return getNombreFileName(tokens[tokens.length-1]);
	}
	private String getExtensionPath(String path) {
		String[] tokens = StringUtils.split("\\");
		return  getExtensionFileName(tokens[tokens.length-1]);
		
	}

	@Override
	public void downloadFile(Fichero fichero) {
		List<MapEntryPhoto> partes = this.mapEntryPhotoService.findByFichero(fichero);
		List<Referencia> referencias = this.photoClient.downloadFichero(partes);
		this.converter.createOriginalFromReferencia(referencias);
		
		
	}

	@Override
	public void delete(Fichero fichero) {
		List<MapEntryPhoto> mapEntryPhotos = this.mapEntryPhotoService.findByFichero(fichero);
		for(MapEntryPhoto entry : mapEntryPhotos) {
			this.mapEntryPhotoService.delete(entry);
		}
		dao.delete(fichero);
	}

	
	@Override
	public void uploadFile(Fichero fichero) {
		List<Referencia> referencias = converter.convertFichero2Png(fichero);
		for(Referencia referencia : referencias) {
			referencia.getEntry().setFichero(fichero);
			this.mapEntryPhotoService.save(referencia.getEntry());
		}
		
	}

	@Override
	public Fichero fileToFichero(File file) {
		Fichero fichero = new Fichero();
		fichero.setFile(file);
		this.completeFichero(fichero);
		return fichero;
	}

	@Override
	public void compareFile(String fileA, String fileB) {
		this.converter.compareFiles("Z:\\App\\InfiniteSpace\\upload\\trincherasruthmalena.PNG","Z:\\App\\InfiniteSpace\\final\\trincherasruthmalena.PNG");
	}

	@Override
	public boolean esPosibleDescargar(Fichero fichero) {
		boolean resultado = true;
		List<MapEntryPhoto> partes = this.mapEntryPhotoService.findByFichero(fichero);
		for(MapEntryPhoto entry: partes){
			if(!mapEntryPhotoService.esPosibleDescargar(entry))
				return false;
		}
		return resultado;
	}

	@Override
	public Fichero findById(Long id) {
		return this.dao.findById(id).get();
	}

}
