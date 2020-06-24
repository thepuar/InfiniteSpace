package es.thepuar.InfiniteSpace.google.rest;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;


@Service
public class PhotoRestClientImpl implements PhotoRestClient {



	private final CloseableHttpClient httpClient = HttpClients.createDefault();
	
	private String token = "4/1AFqPCnmUxbY1VVvX7FnJPnniEyHNTOQ7MJlxprnvQYMj5bJ-09l88UmCjeArCVNqkKV7rkAjQZMN1Lyv4mH0tQ";

	@Override
	public void cargarImagen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inicializaServlet() {
		// TODO Auto-generated method stub
		inicio();
	}

	private void inicio() {

		URIBuilder builder;
		try {

			builder = new URIBuilder("https://accounts.google.com/o/oauth2/v2/auth");

			builder.addParameter("scope", "https://www.googleapis.com/auth/photoslibrary");
			builder.addParameter("access_type", "offline");
			builder.addParameter("include_granted_scopes", "true");
			builder.addParameter("response_type", "code");
			builder.addParameter("state", "state_parameter_passthrough_value");
			builder.addParameter("redirect_uri", "http://localhost:8080/redirect");
			builder.addParameter("client_id",
					"22319061528-res1pd00l3ds037ohkt5ahngf5gi81ib.apps.googleusercontent.com");
			System.out.println(builder.build());
			HttpGet request = new HttpGet(builder.build());
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			Header header = entity.getContentType();
		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void sendPost() {
		
		URIBuilder builder;
		try {
			builder = new URIBuilder("https://photoslibrary.googleapis.com/v1/uploads");
			HttpPost post = new HttpPost(builder.build());
			post.addHeader("Content-type", "application/octet-stream");
			post.addHeader("X-Goog-Upload-Content-Type","image/png");
			post.addHeader("X-Goog-Upload-Protocol","raw");
			post.addHeader("Authorization","Bearer "+this.token);
			 
			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
			entityBuilder.addBinaryBody("media-binary-data", new File("H:\\Documentos\\InfiniteSpace\\zhola.png"));
			HttpEntity multipart = entityBuilder.build();
			post.setEntity(multipart);
			
			CloseableHttpResponse response = httpClient.execute(post);
			String uploadToken = (String) response.getParams().getParameter("upload-token");
			System.out.println(uploadToken);
		}catch(URISyntaxException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getToken() {
		// TODO Auto-generated method stub
		return this.token;
	}

	@Override
	public void setToken(String token) {
		// TODO Auto-generated method stub
		this.token = token;
		
	}
	
}
