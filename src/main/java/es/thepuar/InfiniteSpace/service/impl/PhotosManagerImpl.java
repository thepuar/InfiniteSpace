package es.thepuar.InfiniteSpace.service.impl;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.rpc.ApiException;
import com.google.auth.Credentials;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.UserCredentials;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.PhotosLibrarySettings;
import com.google.photos.types.proto.Album;

import es.thepuar.InfiniteSpace.service.api.PhotosManager;

public class PhotosManagerImpl implements PhotosManager {

	public void inicializacion() {
		
//		UserCredentials.newBuilder().setClientId("562468230154-6lhlfmee7dtkjnbgaj7efmvlokpqn845.apps.googleusercontent.com").setClientSecret("VtTb6Qoit-r3qJmz_yPHbdoa").setAccessToken(new AccessToken("AIzaSyAwVHKXemyil7We_bTy2-67sBTCAVeFTcU", expirationTime)).build();
//		PhotosLibrarySettings settings = PhotosLibrarySettings.newBuilder()
//				.setCredentialsProvider(FixedCredentialsProvider.create(/* Add credentials here. */)).build();
//
//		try (PhotosLibraryClient photosLibraryClient = PhotosLibraryClient.initialize(settings)) {
//
//			// Create a new Album with at title
//			Album createdAlbum = photosLibraryClient.createAlbum("My Album");
//
//			// Get some properties from the album, such as its ID and product URL
//			String id = album.getId();
//			String url = album.getProductUrl();
//
//		} catch (ApiException e) {
//			// Error during album creation
//		}
	}
}
