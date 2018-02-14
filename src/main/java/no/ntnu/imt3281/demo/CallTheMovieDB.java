package no.ntnu.imt3281.demo;

import java.io.IOException;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * The code given for Unirest on developers.themoviedb.org is outdated, this class shows how to get the response from this site using the 
 * Unirest http client.
 * 
 * Documentation for Unirest at : http://unirest.io/java.html
 * 
 * @author oeivindk
 *
 */
public class CallTheMovieDB {
	public static void main(String[] args) {
		String req;
		try {
			req = Unirest.get("https://api.themoviedb.org/3/person/62?language=en-US&api_key=a47f70eb03a70790f5dd711f4caea42d").asString().getBody();
			System.out.println(req);		// Outputs JSON response from server.
			Unirest.shutdown();			// Must be called at the end of the application
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
