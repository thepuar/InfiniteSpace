package es.thepuar.InfiniteSpace;

import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.Referencia;
import es.thepuar.InfiniteSpace.service.impl.FileToPngImplV2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.*;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class FicherosTest {
    String rutaOriginal = "H:\\Peliculas\\Disney\\Coco M1080.www.newpct1.com.mkv";
    //String rutaOriginal = "H:\\Peliculas\\Reposeida (1990) [DVDRip Spa].avi";
    //String rutaOriginal = "H:\\\\Peliculas\\\\One Piece\\\\1-50 OP\\\\One Piece - 001 [SC] [Animelv.net].mp4";
    String rutaConstruido = "Z:\\App\\InfiniteSpace\\final\\Onepiece001.avi";

    @Test
    public void prueba() {

        //String ruta = "H:\\Peliculas\\Disney\\Coco M1080.www.newpct1.com.mkv";
        FileToPngImplV2 v2 = new FileToPngImplV2();
        Fichero fichero = new Fichero();
        fichero.setNombre("Onepiece001");
        fichero.setExtension("mkv");
        fichero.setFile(new File(rutaOriginal));
        fichero.setBytes(new File(rutaOriginal).length());
        List<Referencia> referencias = v2.convertFichero2Png(fichero);
        v2.createOriginalFromReferencia(referencias);
        System.out.println("Fin");
    }

    @Test
    public void compareFiles(){

        File ficheroOriginal = new File(rutaOriginal);
        File ficheroConstruido = new File(rutaConstruido);
        if(ficheroOriginal.length()!=ficheroConstruido.length())
            System.out.println("Los ficheros no son iguales "+ficheroOriginal.length() +" / "+ficheroConstruido.length());
        //Leer byte a byte para encontrar la diferencia
        FileInputStream fisOriginal = null;
        FileInputStream fisConstruido = null;
        try {
            fisOriginal = new FileInputStream(ficheroOriginal);
            fisConstruido = new FileInputStream(ficheroConstruido);
            BufferedInputStream bisOriginal = new BufferedInputStream(fisOriginal);
            BufferedInputStream bisConstruido = new BufferedInputStream(fisConstruido);
            int readOriginal = 0;
            int readConstruido = 0;
            int contadorErrores = 0;
            long contador = 0l;
            while((readOriginal = bisOriginal.read())!=-1 | (readConstruido = bisConstruido.read())!=-1){
                if(readOriginal != readConstruido) {
                    System.out.println("Error en posicion " + contador + " " + readOriginal + " / " + readConstruido);
                    contadorErrores++;
                }
                contador++;
            }
            System.out.println("Errores encontrados "+contadorErrores);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void contadorDeCeros(){
        File ficheroOriginal = new File(rutaOriginal);
        File ficheroConstruido = new File(rutaConstruido);
        if(ficheroOriginal.length()!=ficheroConstruido.length())
            System.out.println("Los ficheros no son iguales "+ficheroOriginal.length() +" / "+ficheroConstruido.length());
        //Leer byte a byte para encontrar la diferencia
        FileInputStream fisOriginal = null;
        FileInputStream fisConstruido = null;
        try {
            fisOriginal = new FileInputStream(ficheroOriginal);
            fisConstruido = new FileInputStream(ficheroConstruido);
            BufferedInputStream bisOriginal = new BufferedInputStream(fisOriginal);
            BufferedInputStream bisConstruido = new BufferedInputStream(fisConstruido);
            int readOriginal = 0;
            int readConstruido = 0;
            int contadorCerosOriginal = 0;
            int contadorCerosConstruido = 0;
            long contador = 0l;
            while((readOriginal = bisOriginal.read())!=-1){
                if(readOriginal == 0) {
                    contadorCerosOriginal++;
                }
            }
            while((readConstruido = bisConstruido.read())!=-1){
                if(readConstruido == 0) {
                    contadorCerosConstruido++;
                }
            }
            System.out.println("Ceros encontrados "+contadorCerosOriginal +" / "+contadorCerosConstruido);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void purgeEmptyDataTest(){
        byte[] buffer = new byte[2097150];
        buffer = startBuffer(buffer);
        buffer = valorBuffer(buffer,0,3,(byte)'a');
        buffer = valorBuffer(buffer,5,7,(byte)'a');
        buffer = valorBuffer(buffer,20,30,(byte)'b');
        FileToPngImplV2 v2 = new FileToPngImplV2();
        byte[] result = v2.purgeEmptyData(buffer,false);
        System.out.println("Primer 0 "+posicionPrimerCero(result));

    }

    public byte[] startBuffer(byte[] buffer){
        for(int i = 0; i<buffer.length;i++){
            buffer[i]=0;
        }
        return buffer;
    }
    public byte[] valorBuffer(byte[] buffer, int inicio, int fin, byte valor){
        for( ; inicio<=fin; inicio++){
            buffer[inicio] = valor;
        }
        return buffer;
    }
    public int posicionPrimerCero(byte[] buffer){
        for(int i = 0; i<buffer.length;i++){
            if(buffer[i]==0)
                return i;
        }
        return -1;
    }


}
