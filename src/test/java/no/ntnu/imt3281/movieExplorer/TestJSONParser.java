package no.ntnu.imt3281.movieExplorer;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestJSONParser {
	private static final String jsonInput = String.join("", "{\n" , 
			"    \"adult\": false,\n" , 
			"    \"backdrop_path\": \"/qlGoGQSVMzIjGbpvXzZUOH1FjNu.jpg\",\n" , 
			"    \"belongs_to_collection\": null,\n" , 
			"    \"budget\": 90000000,\n" , 
			"    \"genres\": [\n" , 
			"        {\n" , 
			"            \"id\": 12,\n" , 
			"            \"name\": \"Adventure\"\n" , 
			"        },\n" , 
			"        {\n" , 
			"            \"id\": 14,\n" , 
			"            \"name\": \"Fantasy\"\n" , 
			"        },\n" , 
			"        {\n" , 
			"            \"id\": 28,\n" , 
			"            \"name\": \"Action\"\n" , 
			"        },\n" , 
			"        {\n" , 
			"            \"id\": 53,\n" , 
			"            \"name\": \"Thriller\"\n" , 
			"        },\n" , 
			"        {\n" , 
			"            \"id\": 878,\n" , 
			"            \"name\": \"Science Fiction\"\n" , 
			"        }\n" , 
			"    ],\n" , 
			"    \"homepage\": \"\",\n" , 
			"    \"id\": 18,\n" , 
			"    \"imdb_id\": \"tt0119116\",\n" , 
			"    \"original_language\": \"en\",\n" , 
			"    \"original_title\": \"The Fifth Element\",\n" , 
			"    \"overview\": \"In 2257, a taxi driver is unintentionally given the task of saving a young girl who is part of the key that will ensure the survival of humanity.\",\n" , 
			"    \"popularity\": 31.304703,\n" , 
			"    \"poster_path\": \"/zaFa1NRZEnFgRTv5OVXkNIZO78O.jpg\",\n" , 
			"    \"production_companies\": [\n" , 
			"        {\n" , 
			"            \"name\": \"Columbia Pictures\",\n" , 
			"            \"id\": 5\n" , 
			"        },\n" , 
			"        {\n" , 
			"            \"name\": \"Gaumont\",\n" , 
			"            \"id\": 9\n" , 
			"        }\n" , 
			"    ],\n" , 
			"    \"production_countries\": [\n" , 
			"        {\n" , 
			"            \"iso_3166_1\": \"FR\",\n" , 
			"            \"name\": \"France\"\n" , 
			"        }\n" , 
			"    ],\n" , 
			"    \"release_date\": \"1997-05-07\",\n" , 
			"    \"revenue\": 263920180,\n" , 
			"    \"runtime\": 126,\n" , 
			"    \"spoken_languages\": [\n" , 
			"        {\n" , 
			"            \"iso_639_1\": \"en\",\n" , 
			"            \"name\": \"English\"\n" , 
			"        },\n" , 
			"        {\n" , 
			"            \"iso_639_1\": \"sv\",\n" , 
			"            \"name\": \"svenska\"\n" , 
			"        },\n" , 
			"        {\n" , 
			"            \"iso_639_1\": \"de\",\n" , 
			"            \"name\": \"Deutsch\"\n" , 
			"        }\n" , 
			"    ],\n" , 
			"    \"status\": \"Released\",\n" , 
			"    \"tagline\": \"There is no future without it.\",\n" , 
			"    \"title\": \"The Fifth Element\",\n" , 
			"    \"video\": false,\n" , 
			"    \"vote_average\": 7.3,\n" , 
			"    \"vote_count\": 3982\n" , 
			"}");

	@Test
	public void test() {
		JSON parsedJSON = new JSON(jsonInput);												// Should parse the provided json string
		assertEquals("The Fifth Element", parsedJSON.getValue("original_title"));				// getValue returns an Object holding the value for the requested key
		assertEquals(90000000L, parsedJSON.getValue("budget"));								// All non floating point numbers are returned as Long
		assertEquals(126L, parsedJSON.getValue("runtime"));
		assertEquals("Adventure", parsedJSON.get("genres").get(0).getValue("name"));			// the get method should return a new JSON object containing the branch for the given name or index
		assertEquals("English", parsedJSON.get("spoken_languages").get(0).getValue("name"));	
		assertEquals(3, parsedJSON.get("spoken_languages").size(), 0);						// The size of the array "spoken_languages"
		assertEquals(2, parsedJSON.get("spoken_languages").get(0).size(), 0);					// The size of the first JSON object in "spoken_languages"

	}
}
