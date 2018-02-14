package no.ntnu.imt3281.movieExplorer;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;


/**
 * Used to search for information from tmdb
 */
public class Search {
    private static String api_key = "a47f70eb03a70790f5dd711f4caea42d";
    public static Database database = new Database();

    /**
     * Only for initializing database.
     * This is because the database wont be initialized until
     * we call a function from this class, and that would mean
     * slowing down the program when the user tries to make their
     * first search if the databse hasn't been created yet,
     * so instead we just run this on startup
     */
    public static void init(){}


    /**
     * Search for some parameter
     * @param searchParam parameter to search
     * @return a JSON object containing the result of the lookup
     */
    public static JSON multiSearch(String searchParam) {
        searchParam = searchParam.replace(" ", "%20");
        String query = "https://api.themoviedb.org/3/search/multi?api_key=" + api_key + "&query=" + searchParam;

        return new JSON(pullData(query));
    }


    /**
     * Looks up all actors related to a certain movie
     * @param movie_id id of movie to look up
     * @return a JSON object containng the result of the lookup
     */
    public static JSON actors(long movie_id) {
        JSON json = database.getMovie(movie_id);
        if(json != null)
            return json;


        String query = "https://api.themoviedb.org/3/movie/" + movie_id + "/credits?api_key=" + api_key;
        json = new JSON(pullData(query));
        database.addMovie(movie_id, json);

        return json;
    }

    /**
     * Looks up all movies in which a person takes part
     * @param person_id id of person to look up
     * @return a JSON object containing the result of the lookup
     */
    public static JSON takesPartIn(long person_id) {
        JSON json = database.getPerson(person_id);
        if(json != null)
            return json;

        String query = "https://api.themoviedb.org/3/discover/movie?api_key=" + api_key + "&language=en-US&sort_by=popularity.desc&with_people=" + person_id;
        json = new JSON(pullData(query));
        database.addPerson(person_id, json);

        return json;
    }


    /**
     * Looks up information on a movie
     * @param id id of movie
     * @return JSON object with info
     */
    public static JSON movie(long id){
        JSON json = database.getMovieInfo(id);
        if(json != null)
            return json;

        json = new JSON(Search.pullData("https://api.themoviedb.org/3/movie/" + id + "?api_key=" + api_key + "&language=en-US"));
        database.addMovieInfo(id, json);
        return json;
    }

    /**
     * Looks up a query with Unirest and returns the result
     * @param query query to look up
     * @return Result from query. Returns "ERROR" if exception is thrown
     */
    public static String pullData(String query){
        String result;
        try {
            result = Unirest.get(query).asString().getBody();
        } catch (UnirestException ue){
            ue.printStackTrace();
            result = "ERROR";
        }

        return result;
    }

    /**
     * Get the api key for looking up information through unirest
     * @return api key
     */
    public static String getAPIKey() {
        return api_key;
    }
}
