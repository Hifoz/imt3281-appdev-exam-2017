package no.ntnu.imt3281.movieExplorer;

import org.json.simple.*;
import org.json.simple.parser.*;

/**
 * A Class used to get json data
 */
public class JSON {
    private JSONObject jsonObject;

    /**
     * Constructor
     * @param input a string containing JSON data
     */
    public JSON(String input){
        try {
            JSONParser parser = new JSONParser();
            Object parsedObject = parser.parse(input);

            if(parsedObject instanceof JSONArray){
                jsonObject = new JSONObject();
                int idx = 0;
                for(Object item : (JSONArray)parsedObject){
                    jsonObject.put(idx, item);
                    idx++;
                }

            } else if (parsedObject instanceof JSONObject){
                jsonObject = (JSONObject)parsedObject;
            }

        } catch (ParseException pe){
            pe.printStackTrace();
        }
    }


    /**
     * Returns the value of an item with a given key
     * @param key key of the value you want to get
     * @return the value
     */
    public Object getValue(String key){
        return jsonObject.get(key);
    }

    /**
     * Returns the value of an item with a given index
     * @param index index of the value you want to get
     * @return the value
     */
    public Object getValue(int index){
        return jsonObject.get(index);
    }



    /**
     * Get a new JSON with a branch of the current, given a key
     * @param key key of branch
     * @return new object
     */
    public JSON get(String key){
        Object json = jsonObject.get(key);
        if(json == null)
            return null;
        return new JSON(jsonObject.get(key).toString());
    }

    /**
     * Get a new JSON with a branch of the current, given an index
     * @param index index of branch
     * @return new object
     */
    public JSON get(int index){
        return new JSON(jsonObject.get(index).toString());
    }

    /**
     * Size of the JSONObject inside
     * @return
     */
    public int size() {
        return jsonObject.size();
    }

    @Override
    public String toString(){
        return jsonObject.toString();
    }

}
