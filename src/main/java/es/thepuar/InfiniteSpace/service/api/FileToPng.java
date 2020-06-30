package es.thepuar.InfiniteSpace.service.api;

import java.util.List;

import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.Referencia;

public interface FileToPng {

	
	public void test();
	
	public void convertFile2Png();
	
	public void createOriginalFromReferencia(List<Referencia> referencias);

	public void createFromImages( List<Referencia> referencias);

	public List<Referencia> convertFichero2Png(Fichero fichero);

	public boolean compareFiles(String a, String b);


}
