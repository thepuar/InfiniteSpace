package es.thepuar.InfiniteSpace.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class ImdbUtil {

    public static String getCover(String nombre){
        OkHttpClient client = new OkHttpClient();
        String tituloPelicula = nombre;
        Request request = new Request.Builder()
                .url("https://imdb-internet-movie-database-unofficial.p.rapidapi.com/search/"+tituloPelicula)
                .get()
                .addHeader("x-rapidapi-host", "imdb-internet-movie-database-unofficial.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "f31ca014b7msh543cbad09febfe8p10655fjsna07e735d90fa")
                .build();

        String image = null;
        try {
            Response response = client.newCall(request).execute();
            Gson gson = new Gson();
            //String stringResponse = gson.fromJson(response.body().string(), String.class);
            JsonObject jsonObject = new JsonParser().parse(response.body().string()).getAsJsonObject();

            image = jsonObject.getAsJsonArray("titles").get(0).getAsJsonObject().get("image").toString();
            image = image.replaceAll("\"","");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
