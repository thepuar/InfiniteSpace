package es.thepuar.InfiniteSpace.model;

import java.io.File;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

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
	private Boolean uploaded;

	@Transient
	private File file;

	@Transient
	public String getRuta() {
		if (file == null)
			return "";
		else
			return file.getAbsolutePath();
	}

	public String getNombreYExtenxion(){
		StringBuilder builder = new StringBuilder();
		builder.append(this.getNombre());
		builder.append(".");
		builder.append(this.getExtension());
		return builder.toString();
	}

}
