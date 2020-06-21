package es.thepuar.InfiniteSpace.service.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import es.thepuar.InfiniteSpace.service.api.FileToPng;

@Service
public class FileToPngImpl implements FileToPng {

	@Override
	public void test() {
		File f = new File("H:\\Documentos\\InfiniteSpace\\zhola.pdf");

		try {
			FileInputStream in = new FileInputStream(f);
			byte[] bytes = IOUtils.toByteArray(in);

			System.out.println("Numero de bytes " + bytes.length);

			byte[] result = completeBytes(bytes);
			System.out.println("Bytes leidos de zhola.pdf " + result.length);
			createPng(result);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private byte[] completeBytes(byte[] bytes) {
		int tamanyo = 1920 * 1080 * 3;
		byte[] result = new byte[tamanyo];
		for (int i = 0; i < tamanyo; i++) {
			if (bytes.length > i) {
				result[i] = bytes[i];
			} else {
				result[i] = 0;
			}
		}
		return result;
	}

	private void createPng(byte[] bytes) {
		int sizeX = 1920;
		int sizeY = 1080;
		int position = 0;
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
		File outputfile = new File("H:\\Documentos\\InfiniteSpace\\zhola.png");
		try {
			ImageIO.write(bufferedImage, "png", outputfile);
			System.out.println("Imagen creada");

			createFromImage(outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void createFromImage(File f) {
		byte[] data = new byte[1920 * 1080 * 3];
		try {
			BufferedImage bufferedImage = ImageIO.read(f);
			int position = 0;
			for (int i = 0; i < 1920; i++) {
				for (int j = 0; j < 1080; j++) {
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

	// byte[] pixels = ((DataBufferByte)
	// bufferedImage.getRaster().getDataBuffer()).getData();

}
