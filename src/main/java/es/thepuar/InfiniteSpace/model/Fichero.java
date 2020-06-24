package es.thepuar.InfiniteSpace.model;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
@Entity
public class Fichero {
	
	@Id
	@GeneratedValue
	private Long Id;
	
	private String nombre;
	private String extension;
	private long bytes;
	private Calendar fechaCreacion;
	private Integer partes;
	
	@Transient
	private MultipartFile ruta;
	
	
}
