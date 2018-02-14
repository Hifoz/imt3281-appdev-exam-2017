package no.ntnu.imt3281.demo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.*;
import org.json.simple.parser.*;


public class ParseJSON {
	
	public static void printJSONObject (JSONObject obj, int level) {
		String tabs = "";
		for (int i=0; i<level; i++) {
			tabs += "\t";
		}
		Set<String> keys = obj.keySet();
		for (String key : keys) {
	    		Object o = obj.get(key);
	    		if (o instanceof JSONArray) {	// Key points to a JSON array
	    			System.out.println (tabs+key+" [");
	    			printJSONArray((JSONArray)o, level+1);
	    			System.out.println (tabs+"]");
	    		} else if (o instanceof JSONObject) {	// Key points to a JSON object
	    			System.out.println (tabs+key+" -> ");
	    			printJSONObject((JSONObject)o, level+1);
	    		} else {	// Key points to a simple value
	    			System.out.println (tabs+key+": "+o);
	    		}
	    	}
	}
	
	private static void printJSONArray(JSONArray arr, int level) {
		String tabs = "";
		for (int i=0; i<level; i++) {
			tabs += "\t";
		}
		Iterator<String> iterator = arr.iterator();
	    	while (iterator.hasNext()) {
	    		System.out.println(tabs+"[");
	    		Object o = iterator.next();
	    		if (o instanceof JSONArray) {	// Key points to a JSON array
	    			System.out.println (tabs+" [");
	    			printJSONArray((JSONArray)o, level+1);
	    			System.out.println (tabs+"]");
	    		} else if (o instanceof JSONObject) {	// Key points to a JSON object
	    			printJSONObject((JSONObject)o, level);
	    		} else {	// Key points to a simple value
	    			System.out.println (tabs+": "+o);
	    		}
	    		System.out.println(tabs+"],");
	    	}
	}

	public static void main(String[] args) {
		try {
		    	// The API at swapi.co is meant to be machine friendly. Returns data as json
		    	JSONParser parser = new JSONParser();
		    	// Can query films/people/planet/species/starships/vehicles
		    	URL url = new URL ("http://musicbrainz.org/ws/2/release/bc2be498-ff42-4a4f-b4df-53d6e3bee74a?inc=artist-credits+labels+discids+recordings&fmt=json");
		    	URLConnection connection = url.openConnection();				// Need a bit extra
		    	// Must set user-agent, the java default user agent is denied
		    	connection.setRequestProperty("User-Agent", "curl/7.8 (i386-redhat-linux-gnu) libcurl 7.8 (OpenSSL 0.9.6b) (ipv6 enabled)");
		    	// Must set accept to application/json, if not html is returned
		    	connection.setRequestProperty("Accept", "application/json");
		    	connection.connect();
	
		    	// Read and parse the response
		    	JSONObject obj = (JSONObject) parser.parse(new InputStreamReader(connection.getInputStream()));
		    	
		    	printJSONObject(obj, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}