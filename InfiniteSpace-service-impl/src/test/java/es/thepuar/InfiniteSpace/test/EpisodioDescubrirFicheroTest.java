package es.thepuar.InfiniteSpace.test;

import es.thepuar.InfiniteSpace.model.Episodio;
import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.service.impl.EpisodioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.AssertionErrors;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest
public class EpisodioDescubrirFicheroTest {
    EpisodioServiceImpl episodioService = new EpisodioServiceImpl();

    //00x00
    @Test
    public void pruebaCadena1(){
        String cadena = "The Walking Dead 10x04 Haz callar los susurros [HDiT+DVB][spanish-english][grupots]";
        Fichero fichero = new Fichero();
        fichero.setNombre(cadena);
        Episodio episodio = episodioService.crearEpisodio(fichero);
        AssertionErrors.assertNotNull("El episodio no puede ser nulo",episodio);
        AssertionErrors.assertNotNull("No hay numero de episodio",episodio);
    }

    //00x00
    @Test
    public void pruebaCadena2(){
        String cadena = "One Piece - 002 [SC] [Animelv.net]";
        Fichero fichero = new Fichero();
        fichero.setNombre(cadena);
        Episodio episodio = episodioService.crearEpisodio(fichero);
        AssertionErrors.assertNotNull("El episodio no puede ser nulo",episodio);
        AssertionErrors.assertNotNull("No hay numero de episodio",episodio);
    }
}
