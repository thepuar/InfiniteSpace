package es.thepuar.InfiniteSpace.model;

import java.io.File;

import lombok.Data;

@Data
public class FicheroDirectorio {

	private int Id;
	private File file;
	
	
	
	public FicheroDirectorio(int id, File file) {
		this.Id = id;
		this.file = file;
	}
	
	public long getKb() {return file.length()/1024;}
}
