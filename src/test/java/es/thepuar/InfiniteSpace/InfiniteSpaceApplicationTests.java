package es.thepuar.InfiniteSpace;

import es.thepuar.InfiniteSpace.manager.ResourceManager;
import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.Referencia;
import es.thepuar.InfiniteSpace.service.api.FicheroService;
import es.thepuar.InfiniteSpace.service.api.FileToPng;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class InfiniteSpaceApplicationTests {

	@Autowired
	FileToPng fileToPng;

	@Autowired
	FicheroService ficheroService;

	@Autowired
	ResourceManager resourceManager;



	//@Test
	void contextLoads() {
		fileToPng.test();
		assertThat(fileToPng).isNotNull();
	}

	@Test
	void compruebaBytes() throws IOException {
		final String defaultpath= "Z:\\App\\InfiniteSpace\\upload\\";
		//fileToPng.test();
		//assertThat(fileToPng).isNotNull();
		List<String> pathFiles = new ArrayList();
		pathFiles.add(defaultpath+"IMG_5547.MOV");
		pathFiles.add(defaultpath+"Matrix - The Animatrix Los 9 Capitulos En Español.avi");


		for(String path  : pathFiles) {
			File f = new File(path);
			FileInputStream in = new FileInputStream(f);
			byte[] byteOriginal = IOUtils.toByteArray(in);
			Fichero fichero = ficheroService.fileToFichero(f);
			List<Referencia> referencias = fileToPng.convertFichero2Png(fichero);
			int contador = 0;

			this.fileToPng.createOriginalFromReferencia(referencias);

			File freconstruido = new File(resourceManager.getProperty("ruta_final") + "\\" + fichero.getNombreYExtenxion());
			FileInputStream inReconstruido = new FileInputStream(freconstruido);
			byte[] byteReconstruido = IOUtils.toByteArray(inReconstruido);

			assertThat(byteOriginal.length).isEqualTo(byteReconstruido.length);
			for (int i = 0; i < byteOriginal.length; i++) {
				if (byteOriginal[i] != byteReconstruido[i])
					System.out.println("Error encontrado en la posicion " + i);
				assertThat(byteOriginal[i]).isEqualTo(byteReconstruido[i]);
			}
		}

	}

}
