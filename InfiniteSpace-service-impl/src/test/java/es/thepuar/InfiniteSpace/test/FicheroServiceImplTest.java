package es.thepuar.InfiniteSpace.test;

import es.thepuar.InfiniteSpace.service.api.FicheroService;
import es.thepuar.InfiniteSpace.service.impl.FicheroServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;

public class FicheroServiceImplTest {


    FicheroServiceImpl ficheroServiceImpl = new FicheroServiceImpl();

    String fichero = "Escuadron Suicida [BluRay Rip][www.newpct1.com].avi";


    @Test
    public void getNombreFileName(){
        String result = ficheroServiceImpl.getNombreFileName(fichero);
        System.out.println(result);
        Assert.isTrue(result.equals("Escuadron Suicida [BluRay Rip][www.newpct1.com]"));
    }

    @Test
    public void getExtensionFileName(){
        String result = ficheroServiceImpl.getExtensionFileName(fichero);
        System.out.println(result);
        Assert.isTrue(result.equals("avi"));
    }

}
