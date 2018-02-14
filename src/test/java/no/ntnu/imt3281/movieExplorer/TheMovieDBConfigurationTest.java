package no.ntnu.imt3281.movieExplorer;

import static org.junit.Assert.assertEquals;

import javafx.stage.Stage;
import org.junit.Test;

public class TheMovieDBConfigurationTest {
	private static String json = String.join("", "{\n",
				"    \"images\": {\n",
				"        \"base_url\": \"http://image.tmdb.org/t/p/\",\n",
				"        \"secure_base_url\": \"https://image.tmdb.org/t/p/\",\n",
				"        \"backdrop_sizes\": [\n",
				"            \"w300\",\n",
				"            \"w780\",\n",
				"            \"w1280\",\n",
				"            \"original\"\n",
				"        ],\n",
				"        \"logo_sizes\": [\n",
				"            \"w45\",\n",
				"            \"w92\",\n",
				"            \"w154\",\n",
				"            \"w185\",\n",
				"            \"w300\",\n",
				"            \"w500\",\n",
				"            \"original\"\n",
				"        ],\n",
				"        \"poster_sizes\": [\n",
				"            \"w92\",\n",
				"            \"w154\",\n",
				"            \"w185\",\n",
				"            \"w342\",\n",
				"            \"w500\",\n",
				"            \"w780\",\n",
				"            \"original\"\n",
				"        ],\n",
				"        \"profile_sizes\": [\n",
				"            \"w45\",\n",
				"            \"w185\",\n",
				"            \"h632\",\n",
				"            \"original\"\n",
				"        ],\n",
				"        \"still_sizes\": [\n",
				"            \"w92\",\n",
				"            \"w185\",\n",
				"            \"w300\",\n",
				"            \"original\"\n",
				"        ]\n",
				"    },\n",
				"    \"change_keys\": [\n",
				"        \"adult\",\n",
				"        \"air_date\",\n",
				"        \"also_known_as\",\n",
				"        \"alternative_titles\",\n",
				"        \"biography\",\n",
				"        \"birthday\",\n",
				"        \"budget\",\n",
				"        \"cast\",\n",
				"        \"certifications\",\n",
				"        \"character_names\",\n",
				"        \"created_by\",\n",
				"        \"crew\",\n",
				"        \"deathday\",\n",
				"        \"episode\",\n",
				"        \"episode_number\",\n",
				"        \"episode_run_time\",\n",
				"        \"freebase_id\",\n",
				"        \"freebase_mid\",\n",
				"        \"general\",\n",
				"        \"genres\",\n",
				"        \"guest_stars\",\n",
				"        \"homepage\",\n",
				"        \"images\",\n",
				"        \"imdb_id\",\n",
				"        \"languages\",\n",
				"        \"name\",\n",
				"        \"network\",\n",
				"        \"origin_country\",\n",
				"        \"original_name\",\n",
				"        \"original_title\",\n",
				"        \"overview\",\n",
				"        \"parts\",\n",
				"        \"place_of_birth\",\n",
				"        \"plot_keywords\",\n",
				"        \"production_code\",\n",
				"        \"production_companies\",\n",
				"        \"production_countries\",\n",
				"        \"releases\",\n",
				"        \"revenue\",\n",
				"        \"runtime\",\n",
				"        \"season\",\n",
				"        \"season_number\",\n",
				"        \"season_regular\",\n",
				"        \"spoken_languages\",\n",
				"        \"status\",\n",
				"        \"tagline\",\n",
				"        \"title\",\n",
				"        \"translations\",\n",
				"        \"tvdb_id\",\n",
				"        \"tvrage_id\",\n",
				"        \"type\",\n",
				"        \"video\",\n",
				"        \"videos\"\n",
				"    ]\n",
				"}");

	@Test
	public void test() {
        // The json data that has been hard coded here would normally be found by requesting : https://api.themoviedb.org/3/configuration?api_key=
		// More information here: https://developers.themoviedb.org/3/configuration  
		TheMovieDBConfiguration configuration = new TheMovieDBConfiguration(json);
		// Each of the getNNNNNURL functions should return the base url appended with the largest image size 
		// and then the parameter sent to the function
		String result = configuration.getBackdropURL("dummy.jpg");
		assertEquals("http://image.tmdb.org/t/p/w1280/dummy.jpg", result);
		result = configuration.getLogoURL("dummy.jpg");
		assertEquals("http://image.tmdb.org/t/p/w500/dummy.jpg", result);
		result = configuration.getPosterURL("dummy.jpg");
		assertEquals("http://image.tmdb.org/t/p/w780/dummy.jpg", result);
		result = configuration.getProfileURL("dummy.jpg");
		assertEquals("http://image.tmdb.org/t/p/h632/dummy.jpg", result);
		result = configuration.getStillURL("dummy.jpg");
		assertEquals("http://image.tmdb.org/t/p/w300/dummy.jpg", result);
	}
}
