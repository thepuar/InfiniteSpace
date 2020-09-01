package es.thepuar.InfiniteSpace;

import es.thepuar.InfiniteSpace.utils.ImdbUtil;
import org.assertj.core.util.Strings;
import org.junit.jupiter.api.Test;
import org.junit.Assert.*;
import org.springframework.util.Assert;

public class RapidApiTest {

    String pelicula ="Robocop 3";

    @Test
    public void getCoverTest(){

    String uri = ImdbUtil.getCover(pelicula);
        Assert.isTrue(!Strings.isNullOrEmpty(uri),"No hay uri");
    }
}
