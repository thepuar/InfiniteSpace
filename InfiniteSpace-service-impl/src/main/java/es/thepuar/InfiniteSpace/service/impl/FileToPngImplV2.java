package es.thepuar.InfiniteSpace.service.impl;

import es.thepuar.InfiniteSpace.manager.ResourceManager;
import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.MapEntryPhoto;
import es.thepuar.InfiniteSpace.model.Referencia;
import es.thepuar.InfiniteSpace.service.api.FileToPng;
import es.thepuar.InfiniteSpace.utils.PrinterUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@Qualifier("fileToPngImplV2")
public class FileToPngImplV2 implements FileToPng {

    private final int sizeX = 1920;
    private final int sizeY = 1080;
    private final int limit = (1024 * 1024 * 2) - 2;// Limite de bytes (Numero de posiciones del array)

    @Override
    public void test() {

    }

    @Override
    public void convertFile2Png() {

    }

    @Override
    public void createOriginalFromReferencia(List<Referencia> referencias) {
        Fichero fichero = referencias.get(0).getEntry().getFichero();

        byte[] toPurge = new byte[sizeX * sizeY * 3];
        int contador = 0;
        int positionCompleted = 0;
        try {
            File ffinal = new File(ResourceManager.getProperty("ruta_final") + "\\" + fichero.getNombreYExtension());
            FileOutputStream fos = new FileOutputStream(ffinal);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            for (Referencia referencia : referencias) {
                PrinterUtil.printParte(referencia.getEntry().getParte());

                toPurge = new byte[sizeX * sizeY * 3];

                File f = new File(referencia.getRuta());
                BufferedImage bufferedImage = ImageIO.read(f);
                int position = 0;
                for (int i = 0; i < sizeX; i++) {
                    for (int j = 0; j < sizeY; j++) {
                        int rgbValue = bufferedImage.getRGB(i, j);
                        Color pixel = new Color(rgbValue);
                        int r = pixel.getRed();
                        int g = pixel.getGreen();
                        int b = pixel.getBlue();
                        toPurge[position++] = (byte) r;
                        toPurge[position++] = (byte) g;
                        toPurge[position++] = (byte) b;
                    }
                }
                byte[] result = null;

                if(contador!=referencias.size()-1)
                    result = purgeEmptyData(toPurge,false);
                else
                    result = purgeEmptyData(toPurge,true);
                contador++;

                bos.write(result);

            }

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createFromImages(List<Referencia> referencias) {

    }

    /**
     * Lee un fichero y lo separa en un array de bytes
     *
     * @param fichero
     * @return
     */
    @Override
    public List<Referencia> convertFichero2Png(Fichero fichero) {
        int partes = (int) (fichero.getFile().length() / limit + 1);
        System.out.println("Creando " + partes + " partes de " + fichero.getNombreYExtension());
        List<Referencia> response = new ArrayList<>();
        File f = fichero.getFile();
        List<byte[]> listBytes = new ArrayList<>();

        int contadorBytes = 0;
        try {
            byte[] buffer;
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ByteArrayOutputStream ous = new ByteArrayOutputStream();


            int read = 0;
            int contadorLimite = 0;
            int contadorFichero = 0;
            int contadorPartes = 1;

            while (read != -1) {
                buffer = new byte[limit];
                while (contadorLimite < limit && ((read = bis.read()) != -1)) {
                    ous.write(read);
                    contadorFichero++;
                    contadorLimite++;
                    contadorBytes++;
                }

                //  System.out.println("Parte "+contadorPartes++ +"Se han leido "+contadorLimite + "Bytes");
                contadorLimite = 0;
                buffer = ous.toByteArray();
                ous.reset();

                Referencia referencia = createPng(completeBytesToNull(buffer), contadorPartes++);
                referencia.getEntry().setFichero(fichero);
                response.add(referencia);
            }

            ous.close();
            bis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ContadorBytes " + contadorBytes + " / " + f.length());
        return response;
    }

    @Override
    public boolean compareFiles(String a, String b) {
        return false;
    }

    private Referencia createPng(byte[] bytes, int parte) {
        int position = 0;

        int contador = 0;

        // Constructs a BufferedImage of one of the predefined image types.
        BufferedImage bufferedImage = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        Color color = null;

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {

                int v1 = Byte.toUnsignedInt(bytes[position++]);
                int v2 = Byte.toUnsignedInt(bytes[position++]);
                int v3 = Byte.toUnsignedInt(bytes[position++]);
                color = new Color(v1, v2, v3);
                int rgb = color.getRGB();
                bufferedImage.setRGB(i, j, rgb);

            }
        }

        String url = ResourceManager.getProperty("ruta_temp") + "\\temp" + Calendar.getInstance().getTimeInMillis() + ".png";
        File outputfile = new File(url);
        MapEntryPhoto entry = new MapEntryPhoto();
        entry.setParte(parte);
        Referencia result = new Referencia(url, entry);
        try {
            ImageIO.write(bufferedImage, "png", outputfile);
            PrinterUtil.printParte(parte);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public byte[] completeBytesToNull(byte[] buffer) {
        int numBytesMax = sizeX * sizeY * 3;

        byte[] result = new byte[numBytesMax];
        for (int i = 0; i < numBytesMax; i++) {
            if (i < buffer.length) {
                result[i] = buffer[i];
            } else {
                result[i] = 0;
            }
        }
        return result;

    }

    /**
     * Recibe todos los bytes de un png y se encarga de recuperar solo los validos.
     *
     * @param bytes Recibe los bytes de un png
     * @return
     */
    public byte[] purgeEmptyData(byte[] bytes,boolean esUltimo) {
        byte[] result = null;
        boolean encontradoFin = false;
        int posicionFinal = -1;

        for (int i = 0; i < bytes.length && !encontradoFin; i++) {

            if (posicionFinal == -1 && bytes[i] == 0) {
                if(esElFin(bytes,i)) {
                    posicionFinal = i;
                }
            } else if (posicionFinal != -1 && bytes[i] != 0) {
                // }else if(esElFin(bytes,i)){
                posicionFinal = -1;

            }
        }
        if(!esUltimo){
            //No es el ultimo, asi que rellenar a ceros los ultimos espacios
            posicionFinal = limit;
        }else{
            System.out.println("Es el ultimo");
        }

        if (posicionFinal != -1) {
            result = new byte[posicionFinal];
            for (int i = 0; i < posicionFinal; i++) {
                if(i<result.length)
                result[i] = bytes[i];
                else if(!esUltimo){
                    result[i]=0;
                }
            }
        }else{
            System.out.println("Pasa algo en el fichero ");
        }


        return result;
    }

    /**
     * Comprueba si una posicion de un array es el fin
     *
     * @param buffer
     * @param posicion
     * @return
     */
    public boolean esElFin(byte[] buffer, int posicion) {
        boolean fin = true;
        for (posicion = posicion; posicion < buffer.length && fin; posicion++) {
            if (buffer[posicion] != 0)
                fin = false;


        }
        return fin;
    }
}
