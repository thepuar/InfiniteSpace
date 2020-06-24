package es.thepuar.InfiniteSpace.service.impl;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.thepuar.InfiniteSpace.dao.FicheroDAO;
import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.service.api.FicheroService;

@Service
public class FicheroServiceImpl implements FicheroService{

	@Autowired
	FicheroDAO dao;
	
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
			fichero.setNombre(this.getNombreFileName(fichero.getRuta().getOriginalFilename()));
		}
		
		if(StringUtils.isBlank(fichero.getExtension())) {
			fichero.setExtension(this.getExtensionFileName(fichero.getRuta().getOriginalFilename()));
		}
		fichero.setBytes(fichero.getRuta().getSize());
		fichero.setFechaCreacion(Calendar.getInstance());
		fichero.setPartes(1);
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

}
