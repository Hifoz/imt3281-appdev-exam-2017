package no.ntnu.imt3281.movieExplorer;

import static org.junit.Assert.*;

import org.junit.Test;

public class SearchTest {


	@Test
	public void testMultiSearch() {
		// Multisearch should use the search feature described on https://developers.themoviedb.org/3/search/multi-search
		// Use the search string suggested on that page (i.e. sort result by popularity)
		// Return the result as a JSON object.
		JSON result = Search.multiSearch("Bruce Willis");
		assertEquals(2, result.get("results").size());									// A search for Bruce Willis return two results
		assertEquals("Bruce Willis", result.get("results").get(1).getValue("name"));		// The first is to Bruce Willis
		assertEquals("person", result.get("results").get(1).getValue("media_type"));		// the person
	}
	
	@Test
	public void testActorSearch() {
		// Actors should use the search feature described on https://developers.themoviedb.org/3/movies/get-movie-credits
		// Use the search string suggested on that page (i.e. sort result by popularity)
		// Return the result as a JSON object.
		JSON result = Search.actors(680); 													// Find actors in movie 680 (Pulp Fiction)
		assertEquals("John Travolta", result.get("cast").get(0).getValue("name"));		// John Travolta is the lead actor in this movie
		assertEquals(8891L, result.get("cast").get(0).getValue("id"));					// Person id (in themoviedb)
		assertEquals(54, result.get("cast").size());										// 54 actors in this movie
		assertEquals("Quentin Tarantino", result.get("crew").get(0).getValue("name"));	// First person in list of crew
		assertEquals(138L, result.get("crew").get(0).getValue("id"));						// Person id (in themoviedb)
	}
	
	@Test
	public void testTakesPartInSearch() {
		// TakesPartIn should use the search feature described on https://developers.themoviedb.org/3/discover
		// Use the search string suggested on that page (i.e. sort result by popularity)
		// Use the with_people field to supply the id of the person of interest.
		// Return the result as a JSON object.
		JSON result = Search.takesPartIn(138);
		assertEquals("Pulp Fiction", 
				result.get("results").get(0).getValue("original_title"));					// Quentin Tarantino is director of this movie
		assertEquals(68L, (Long)result.getValue("total_results"), 5L);					// Was 68 on Oct. 19th, should not be more than 5 more/less by time of the exam
	}
}
