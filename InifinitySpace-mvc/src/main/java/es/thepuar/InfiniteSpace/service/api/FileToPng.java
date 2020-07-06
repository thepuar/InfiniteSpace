package es.thepuar.InfiniteSpace.service.api;

import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.Referencia;

import java.util.List;



public interface FileToPng {

	
	public void test();
	
	public void convertFile2Png();
	
	public void createOriginalFromReferencia(List<Referencia> referencias);

	public void createFromImages( List<Referencia> referencias);

	public List<Referencia> convertFichero2Png(Fichero fichero);

	public boolean compareFiles(String a, String b);


}
