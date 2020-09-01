package es.thepuar.InfiniteSpace;

import es.thepuar.InfiniteSpace.utils.PrinterUtil;
import org.junit.jupiter.api.Test;

public class PrinterUtilTest {

    @Test
    public void contador(){
        int max = 1000;
        for(int i = 0; i<max;i++){
            PrinterUtil.printParte(i,max);
        }
    }
}
