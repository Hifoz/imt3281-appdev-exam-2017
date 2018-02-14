package no.ntnu.imt3281.movieExplorer;

import java.io.StringReader;
import java.sql.*;

/**
 * Database
 */
public class Database {
    private boolean connected = false;
    private String databaseURL = "jdbc:derby:Database";
    private Connection conn;
    private Statement statement;

    /**
     * Connect to database
     * Create database if non-existing
     */
    public Database(){
        while(!connected){
            try{
                connectToDB();
            } catch (IllegalStateException ise){
                createDB();
                connectToDB();
            }
        }
    }

    /**
     * Attempts to connect to database
     */
    private void connectToDB() throws IllegalStateException {
        try{
            conn = DriverManager.getConnection(databaseURL);
            statement = conn.createStatement();
            connected = true;
        } catch (SQLException sqle){
            //sqle.printStackTrace();
            throw new IllegalStateException("database does not exist");
        }
    }

    /**
     * Sets up a new database
     */
    private void createDB() {
        try {
            conn = DriverManager.getConnection(databaseURL + ";create=true");
            statement = conn.createStatement();

            statement.execute("CREATE TABLE movies (movie_id int NOT NULL UNIQUE,"
                                      + "datas clob NOT NULL,"
                                      + "info clob)");

            statement.execute("CREATE TABLE tvshows (tv_id int NOT NULL UNIQUE,"
                                      + "datas clob NOT NULL)");


            statement.execute("CREATE TABLE persons (person_id int NOT NULL UNIQUE,"
                                      + "datas clob NOT NULL)");


            statement.close();
            conn.close();

            System.out.println("Database created");

        } catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }


    /**
     * Add an entry to the database
     * @param id id of entry
     * @param json the json to store in the entry
     * @param media_type decides which table the entry goes in
     */
    private void addToDB(long id, JSON json, String media_type){
        if(!connected)
            return;

        try{
            PreparedStatement prepStatement = conn.prepareStatement("INSERT INTO " + media_type + "s(" + media_type + "_id, datas) VALUES(?, ?)");
            prepStatement.setInt(1, (int)id);
            prepStatement.setClob(2, new StringReader(json.toString()));
            prepStatement.execute();

        } catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    /**
     * Add an entry to the person table
     * @param id id of entry
     * @param json the json to store in the entry
     */
    public void addPerson(long id, JSON json){
        addToDB(id, json, "person");
    }

    /**
     * Add an entry to the tvshow table
     * @param id id of entry
     * @param json the json to store in the entry
     */
    public void addTvShow(long id, JSON json){
        addToDB(id, json, "tvshow");

    }

    /**
     * Add an entry to the movie table
     * @param id id of entry
     * @param json the json to store in the entry
     */
    public void addMovie(long id, JSON json){
        addToDB(id, json, "movie");
    }

    /**
     * Adds extra movie info to the movie table
     * @param id id of movie
     * @param json json to add to the entry
     */
    public void addMovieInfo(long id, JSON json){
        if(!connected)
            return;

        try{
            PreparedStatement prepStatement = conn.prepareStatement("UPDATE movies SET info=? WHERE movie_id=?");
            prepStatement.setClob(1, new StringReader(json.toString()));
            prepStatement.setInt(2, (int)id);
            prepStatement.execute();

        } catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }


    /**
     * Checks whether there is an entry in the database for id in a table
     * @param id id of entry you want to look for
     * @param media_type decides what table is checked
     * @return returns the JSON stored in the entry, or null if no entry is found
     */
    private JSON getFromDB(long id, String media_type){
        if(!connected)
            return null;

        JSON json = null;
        String query = "SELECT * FROM "+media_type+"s WHERE "+media_type+"_id=?";
        PreparedStatement prepStatement;
        try{
            prepStatement = conn.prepareStatement(query);
            prepStatement.setInt(1, (int)id);
            ResultSet result = prepStatement.executeQuery();

            if(!result.next()) {
                return null;
            } else {
                Clob clob = result.getClob("datas");
                json = new JSON(clob.getSubString(1, (int)clob.length()));
            }

        } catch (SQLException sqle){
            sqle.printStackTrace();
        }

        return json;
    }

    /**
     * Checks whether there is an entry in the person-table for id
     * @param id id of entry you want to look for
     * @return returns the JSON stored in the entry, or null if no entry is found
     */
    public JSON getPerson(long id){
        return getFromDB(id, "person");
    }

    /**
     * Checks whether there is an entry in the tvshow-table for id
     * @param id id of entry you want to look for
     * @return returns the JSON stored in the entry, or null if no entry is found
     */
    public JSON getTvShow(long id){
        return getFromDB(id, "tvshow");
    }

    /**
     * Checks whether there is an entry in the movie-table for id
     * @param id id of entry you want to look for
     * @return returns the JSON stored in the entry, or null if no entry is found
     */
    public JSON getMovie(long id){
        return getFromDB(id, "movie");
    }

    /**
     * Checks wether there is an entry for a movie, and if it has the extra info added yet
     * @param id id of entry
     * @return returns the JSON stored in the entry, or null if no entry is found or entry doesn't have the extra info
     */
    public JSON getMovieInfo(long id){
        if(!connected)
            return null;

        JSON json = null;
        String query = "SELECT * FROM movies WHERE movie_id=? AND info IS NOT NULL";
        PreparedStatement prepStatement;
        try{
            prepStatement = conn.prepareStatement(query);
            prepStatement.setInt(1, (int)id);
            ResultSet result = prepStatement.executeQuery();

            if(!result.next()) {
                return null;
            } else {
                // From http://www.java2s.com/Code/JavaAPI/java.sql/ClobgetSubStringlongposintlength.htm
                Clob clob = result.getClob("info");
                json = new JSON(clob.getSubString(1, (int)clob.length()));
            }

        } catch (SQLException sqle){
            sqle.printStackTrace();
        }

        return json;
    }

    /**
     * Truncate all database tables
     */
    public void clear() {
        String[] data_tables = new String[]{"persons", "movies", "tvshows"};
        for(String tbl : data_tables){
            String query = "TRUNCATE TABLE "+tbl;
            try {
                statement = conn.createStatement();
                statement.execute(query);
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }

        }

    }
}
