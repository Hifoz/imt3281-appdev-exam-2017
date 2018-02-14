package no.ntnu.imt3281.movieExplorer;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestGenres {

	@Test
	public void test() {
		// The Genres class should get its information from the API's described on : https://developers.themoviedb.org/3/genres
		// The genres should be stored in a table in the database so that when requesting names for genre id's that have been previously read
		// from themoviedb no new request to the API is performed. At the very least the data should be stored internally in the class
		// so the information is fetched only once every execution of the program.
		// That is, only request information from themoviedb when an unknown genre id is presented.
		String genre = Genres.resolve(28);
		assertEquals ("Action", genre);					// From movies list
		assertEquals ("Soap", Genres.resolve(10766));	// From tv list
	}
}
