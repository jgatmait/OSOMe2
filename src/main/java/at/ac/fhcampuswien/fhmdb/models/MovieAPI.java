package at.ac.fhcampuswien.fhmdb.models;

import com.google.gson.Gson;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieAPI {

    private static final String BASE = "http://prog2.fh-campuswien.ac.at/movies";
    private static final String DELI = "&";
    private static OkHttpClient client = new OkHttpClient();

    private static String makeURL(String query, String genre, String releaseYear, String ratingFrom) {
        StringBuilder url = new StringBuilder(BASE);

        if ((query != null && !query.isEmpty()) || genre != null || releaseYear != null || ratingFrom != null) {
            url.append("?");
            if (query != null && !query.isEmpty()) {
                url.append("query=").append(query).append(DELI);
            }
            if (genre != null && !genre.equals("No filter")) {
                url.append("genre=").append(genre).append(DELI);
            }
            if (releaseYear != null && !releaseYear.equals("No filter")) {
                url.append("releaseYear=").append(releaseYear).append(DELI);
            }
            if (ratingFrom != null) {
                url.append("ratingFrom=").append(ratingFrom).append(DELI);
            }
        }
        return url;
    }
      private static String makeUrl(String id){
        StringBuilder url = new StringBuilder(BASE);
        if(id != null){
            url.append("/").append(id);
        }
        return url;
    }

    public static String requestUrl(String madeUrl){
        System.out.println("lets make the request @ " +madeUrl);
        Request requesthttp = new Request.Builder()
                //   .addHeader("accept", "application/json")
                .addHeader("User-Agent", "http.agent") //Tutorium 24.3.23
                .url(madeUrl) // <- Finally put httpUrl in here
                .build();
        try(Response response = client.newCall(requesthttp).execute()){
            return response.body().string();
        }catch (Exception ex){
            System.out.println("GET MovieDataBase did not work out");
            ex.printStackTrace();
        }
        return null;
    }

    public static Movie parseMovieToJson (String requestedDataByUrl){
        Movie outputMovie = new Gson().fromJson(requestedDataByUrl, Movie.class); //https://www.baeldung.com/jackson-vs-gson 6.4.23
     return Arrays.asList(outputMovie); //https://www.geeksforgeeks.org/conversion-of-array-to-arraylist-in-java/ 6.4.23
    }

      public static Movie getThatMovieSpecificDown(String id){
        String newUrl = makeUrl(id);
        String getData = requestUrl(newUrl);
        Movie parsedMovie=parseMovieToJson(getData);
        return parsedMovie;
    }

public static List<Movie> getAllMoviesDown(String BASE){
        return parseMovieListToJson(requestUrl(BASE));
}

public static List<Movie> getThatMovieListDown (String queryText, String chosenGenre,
                                                String chosenReleaseYear, String chosenRatingFrom){
        String newUrl = makeUrl( queryText, chosenGenre, chosenReleaseYear, chosenRatingFrom);
        String getData = requestUrl(newUrl);
        List<Movie> parsedMovielist=parseMovieListToJson(getData);
        return parsedMovielist;
        }
}
