package es.thepuar.InfiniteSpace.service.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.imageio.ImageIO;

import es.thepuar.InfiniteSpace.manager.ResourceManager;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.MapEntryPhoto;
import es.thepuar.InfiniteSpace.model.Referencia;
import es.thepuar.InfiniteSpace.service.api.FileToPng;

@Service
public class FileToPngImpl implements FileToPng {

    private final int sizeX = 1920;
    private final int sizeY = 1080;
    private final int limit = (1024 * 1024 * 2) - 1;// Limite de bytes (Numero de posiciones del array)



    /*
     * (non-Javadoc)
     *
     * @see es.thepuar.InfiniteSpace.service.api.FileToPng#test() Funciona cuando el
     * fichero ocupa menos de 2 megas
     */
    @Override
    public void test() {
        File f = new File("H:\\Documentos\\InfiniteSpace\\zhola.pdf");

        try {
            FileInputStream in = new FileInputStream(f);
            byte[] bytes = IOUtils.toByteArray(in);
            in.close();
            System.out.println("Numero de bytes " + bytes.length);

//			byte[] result = completeBytes(bytes);
//			System.out.println("Bytes leidos de zhola.pdf " + result.length);
//			createPng(result);

            // createFromImage(null);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     * Este metodo crea los arrays de datos para las imagenes, Recibe los bytes que tiene que poner y el resto lo completa a 0
     * */
    private byte[] completeBytes(byte[] bytes, int position) {
        boolean primer_null = false;

        int tamanyo = sizeX * sizeY * 3;
        int mylimit = limit < (bytes.length - position) ? limit : (bytes.length - position) ;
        byte[] result = new byte[tamanyo];
        int contador = 0;
        for (int i = position; i < (limit + position) ; i++) {
            if (i == tamanyo - 1)
                System.out.println("Para");
            if (contador < mylimit) {
                result[contador] = bytes[i];
            } else {
                if(!primer_null){
                    primer_null = true;
                    System.out.println("Contador de bytes para el primer null posicion-> "+contador);
                }

                result[contador] = 0;
            }
            contador++;
        }
        return result;
    }

    /**
     * Crea una imagen a partir de arrays de bytes
     * @param bytes
     * @param parte
     * @return
     */
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

            System.out.println("Imagen creada " + url);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    /**
     * Crea un fichero a partir de imagenes
     * @param referencias
     */
    public void createFromImages(List<Referencia> referencias) {
        Fichero fichero = referencias.get(0).getEntry().getFichero();
        byte[] data = new byte[(int) fichero.getBytes()];
        byte[] toPurge = new byte[sizeX * sizeY * 3 * referencias.size()];
        int position = 0;
        int positionCompleted = 0;
        try {
            for (Referencia referencia : referencias) {
                File file = new File(referencia.getRuta());

                BufferedImage bufferedImage = ImageIO.read(file);
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
                byte[] result = purgeEmptyData(toPurge);
            }

            File ffinal = new File(ResourceManager.getProperty("ruta_final") + "\\" + fichero.getNombreYExtenxion());
            FileOutputStream fos = new FileOutputStream(ffinal);
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Metodo para TEST
     * @param f
     */
    private void createFromImage(File f) {
        byte[] data = new byte[sizeX * sizeY * 3];

        try {
            BufferedImage bufferedImage = ImageIO.read(f);
            int position = 0;
            for (int i = 0; i < sizeX; i++) {
                for (int j = 0; j < sizeY; j++) {
                    int rgbValue = bufferedImage.getRGB(i, j);
                    Color pixel = new Color(rgbValue);
                    int r = pixel.getRed();
                    int g = pixel.getGreen();
                    int b = pixel.getBlue();
                    data[position++] = (byte) r;
                    data[position++] = (byte) g;
                    data[position++] = (byte) b;
                }
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            File ffinal = new File("H:\\Documentos\\InfiniteSpace\\zfinal.pdf");
            FileOutputStream fos = new FileOutputStream(ffinal);
            byte[] result = purgeEmptyData(data);
            fos.write(result);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createOriginalFromReferencia(List<Referencia> referencias) {
        Fichero fichero = referencias.get(0).getEntry().getFichero();

        byte[] data = new byte[Math.toIntExact(fichero.getBytes())];

        byte[] toPurge = new byte[sizeX * sizeY * 3];

        int positionCompleted = 0;
        try {
            File ffinal = new File(ResourceManager.getProperty("ruta_final") + "\\" + fichero.getNombreYExtenxion());
            FileOutputStream fos = new FileOutputStream(ffinal);

            for (Referencia referencia : referencias) {
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

                byte[] result = purgeEmptyData(toPurge);

                for (int i = 0; i < result.length; i++) {
                    data[positionCompleted + i] = result[i];
                }
                positionCompleted += result.length;

            }
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Recibe todos los bytes de un png y se encarga de recuperar solo los validos.
     * @param bytes Recibe los bytes de un png
     * @return
     */
    private byte[] purgeEmptyData(byte[] bytes) {
        byte[] result = null;
        boolean encontradoFin = false;
        int posicionFinal = -1;
        for (int i = 0; i < bytes.length && !encontradoFin; i++) {

            if (posicionFinal == -1 && bytes[i] == 0) {
                posicionFinal = i;
            } else if (posicionFinal != -1 && bytes[i] != 0) {
                posicionFinal = -1;

            }
        }
        if (posicionFinal != -1) {
            result = new byte[posicionFinal];
            for (int i = 0; i < posicionFinal; i++) {
                result[i] = bytes[i];
            }
        }
        System.out.println("Numero de bytes tras la purga " + posicionFinal);
        return result;
    }

    /**
     * Meotdo para TEST
     */
    @Override
    public void convertFile2Png() {
        createFromImage(new File("H:\\Documentos\\InfiniteSpace\\zfinal2.png"));

    }

    /**
     * Crea un fichero en PNGs
     * @param fichero
     * @return
     */
    @Override
    public List<Referencia> convertFichero2Png(Fichero fichero) {
        List<Referencia> response = new ArrayList<>();
        File f = fichero.getFile();

        try {

            FileInputStream in = new FileInputStream(f);
            byte[] bytes = IOUtils.toByteArray(in);
            in.close();
            System.out.println("Numero de bytes totales de "+fichero.getNombre()+" "+ bytes.length);
            int parte = 1;
            for (int i = 0; i < bytes.length; i += limit) {
                //Bytes para crear la imagen
                byte[] result = completeBytes(bytes, i);

                Referencia referencia = createPng(result, parte++);
                referencia.getEntry().setFichero(fichero);
                response.add(referencia);
            }

            fichero.setPartes(--parte);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }

    @Override
    public boolean compareFiles(String a, String b) {
        File fa = new File(a);
        File fb = new File(b);
        int diferencia = 1;
        try {
            FileInputStream inA = new FileInputStream(fa);
            FileInputStream inB = new FileInputStream(fb);

            byte[] bytesA = IOUtils.toByteArray(inA);
            byte[] bytesB = IOUtils.toByteArray(inB);

            if (bytesA.length != bytesB.length)
                System.out.println("No tienen el mismo tamaño");
            else {

                for (int i = 0; i < bytesA.length; i++) {
                    if (bytesA[i] != bytesB[i]) {
                        System.out.println("Diferencia: " + diferencia++ + " A-> " + bytesA[i] + " B-> "+bytesB[i]);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (diferencia == 1) return false;
        return true;
    }

}
