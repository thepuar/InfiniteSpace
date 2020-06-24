package es.thepuar.InfiniteSpace.google.rest;

public interface PhotoRestClient {
	
	public String getToken();
	
	public void setToken(String token);
	
	public void cargarImagen();
	
	public void inicializaServlet();
	
	public void sendPost();
	
	

}
