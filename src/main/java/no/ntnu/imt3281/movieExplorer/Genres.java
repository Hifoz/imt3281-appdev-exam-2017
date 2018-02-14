package no.ntnu.imt3281.movieExplorer;

import java.util.HashMap;

public class Genres {
    private static HashMap<Long, String> genres;
    private static String api_key = "a47f70eb03a70790f5dd711f4caea42d";

    /**
     * Gets the name of a genre given an id
     * @param id id of genre
     * @return the name of the genre
     */
    public static String resolve(long id) {
        if(genres == null || genres.isEmpty()){
            init();
        }

        return genres.getOrDefault(id, "Genre not found");
    }

    /**
     * Initializes the genre list
     */
    private static void init(){
        genres = new HashMap<>();
        JSON json;
        for(String media_type : new String[]{"movie", "tv"}){
            json = new JSON(Search.pullData("https://api.themoviedb.org/3/genre/" + media_type + "/list?api_key=" + api_key + "&language=en-US"));
            for(int i = 0; i < json.get("genres").size(); i++){
                JSON genre = json.get("genres").get(i);
                genres.put((long)genre.getValue("id"), (String)genre.getValue("name"));
            }
        }
    }
}
