package es.thepuar.InfiniteSpace.google.client;

import com.google.api.client.googleapis.media.MediaHttpDownloader.DownloadState;
import com.google.api.gax.rpc.ApiException;
import com.google.common.collect.ImmutableList;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.proto.BatchCreateMediaItemsResponse;
import com.google.photos.library.v1.proto.NewMediaItem;
import com.google.photos.library.v1.proto.NewMediaItemResult;
import com.google.photos.library.v1.upload.UploadMediaItemRequest;
import com.google.photos.library.v1.upload.UploadMediaItemResponse;
import com.google.photos.library.v1.util.NewMediaItemFactory;
import com.google.photos.types.proto.MediaItem;
import com.google.rpc.Code;
import com.google.rpc.Status;
import es.thepuar.InfiniteSpace.factory.PhotosLibraryClientFactory;
import es.thepuar.InfiniteSpace.manager.ResourceManager;
import es.thepuar.InfiniteSpace.model.MapEntryPhoto;
import es.thepuar.InfiniteSpace.model.Referencia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Service
public class PhotoClientJava {

	private static final List<String> REQUIRED_SCOPES = ImmutableList.of(
			"https://www.googleapis.com/auth/photoslibrary.readonly",
			"https://www.googleapis.com/auth/photoslibrary.appendonly");

	@Autowired
	ResourceManager resourceManager;

	private PhotosLibraryClient client = null;

	private String myId;

	@PostConstruct
	public void initialize() {
		try {
			client = PhotosLibraryClientFactory.createClient(resourceManager.getProperty("ruta_credential")+ "\\"+resourceManager.getProperty("fichero_credenciales"),
					REQUIRED_SCOPES);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
	}

	public PhotosLibraryClient getClient() {
		return this.client;
	}

	public void uploadFiles(List<Referencia> referencias){
		UploadMediaItemRequest uploadRequest;
		RandomAccessFile file = null;

		for(Referencia referencia : referencias){
			try {
				file = new RandomAccessFile(referencia.getRuta(), "r");
				uploadRequest = UploadMediaItemRequest.newBuilder().setMimeType("image/png").setDataFile(file).build();
				UploadMediaItemResponse uploadResponse = this.client.uploadMediaItem(uploadRequest);
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (uploadResponse.getError().isPresent()) {
					UploadMediaItemResponse.Error error = uploadResponse.getError().get();
					error.getCause().printStackTrace();
				}else {
					String uploadToken = uploadResponse.getUploadToken().get();
					System.out.println("Token " + uploadToken);

					try {
						NewMediaItem newMediaItem = NewMediaItemFactory.createNewMediaItem(uploadToken, "zhola",
								"No soy un pdf");
						List<NewMediaItem> newItems = Arrays.asList(newMediaItem);

						BatchCreateMediaItemsResponse response = this.client.batchCreateMediaItems(newItems);
						for (NewMediaItemResult itemsResponse : response.getNewMediaItemResultsList()) {
							Status status = itemsResponse.getStatus();
							if (status.getCode() == Code.OK_VALUE) {
								MediaItem createdItem = itemsResponse.getMediaItem();
								System.out.println("Subido Id " + createdItem.getId());
								this.myId = createdItem.getId();
								referencia.getEntry().setMediaId(myId);
							} else {
								// The item could not be created. Check the status and try again
							}
						}
					} catch (ApiException e) {
						e.printStackTrace();
					}

				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public void uploadFile() {

		UploadMediaItemRequest uploadRequest;

		RandomAccessFile file = null;
		try {
			file = new RandomAccessFile("H:\\Documentos\\InfiniteSpace\\zhola.png", "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		uploadRequest = UploadMediaItemRequest.newBuilder().setMimeType("image/png").setDataFile(file).build();

		UploadMediaItemResponse uploadResponse = this.client.uploadMediaItem(uploadRequest);
		try {
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (uploadResponse.getError().isPresent()) {
			// If the upload results in an error, handle it
			UploadMediaItemResponse.Error error = uploadResponse.getError().get();
			error.getCause().printStackTrace();
		} else {
			// If the upload is successful, get the uploadToken
			String uploadToken = uploadResponse.getUploadToken().get();
			System.out.println("Token " + uploadToken);
			// Use this upload token to create a media item

			try {
				// Create a NewMediaItem with the following components:
				// - uploadToken obtained from the previous upload request
				// - filename that will be shown to the user in Google Photos
				// - description that will be shown to the user in Google Photos
				NewMediaItem newMediaItem = NewMediaItemFactory.createNewMediaItem(uploadToken, "zhola",
						"No soy un pdf");
				List<NewMediaItem> newItems = Arrays.asList(newMediaItem);

				BatchCreateMediaItemsResponse response = this.client.batchCreateMediaItems(newItems);
				for (NewMediaItemResult itemsResponse : response.getNewMediaItemResultsList()) {
					Status status = itemsResponse.getStatus();
					if (status.getCode() == Code.OK_VALUE) {
						// The item is successfully created in the user's library
						MediaItem createdItem = itemsResponse.getMediaItem();
						System.out.println("Subido Id " + createdItem.getId());
						this.myId = createdItem.getId();
					} else {
						// The item could not be created. Check the status and try again
					}
				}
			} catch (ApiException e) {
				// Handle error
				e.printStackTrace();
			}

		}

	}

	public List<Referencia> downloadFichero(List<MapEntryPhoto> partes) {

		List<Referencia> result = new ArrayList<>();
		for (MapEntryPhoto entry : partes) {
			MediaItem item = this.client.getMediaItem(entry.getMediaId());
			String url = item.getBaseUrl() + "=w1920-h1080";
			String ruta = resourceManager.getProperty("ruta_descarga")+"\\" + Calendar.getInstance().getTimeInMillis() + ".png";
			result.add(new Referencia(ruta, entry));
			this.downloadFromUrl(url, ruta);
		}
		return result;
	}

	public void downloadImage() {
		MediaItem item = this.client.getMediaItem(this.myId);
		String url = item.getBaseUrl() + "=w1920-h1080";
		System.out.println("URL: " + url);

		this.downloadFromUrl(url, resourceManager.getProperty("ruta_descarga")+"\\" + Calendar.getInstance().getTimeInMillis() + ".png");
	}

	public void downloadFromUrl(String url, String file) {

		try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());

				FileOutputStream fileOutputStream = new FileOutputStream(file)) {
			byte dataBuffer[] = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
				fileOutputStream.write(dataBuffer, 0, bytesRead);
			}
		} catch (IOException e) {
			// handle exception
			e.printStackTrace();
		}

	}
}
