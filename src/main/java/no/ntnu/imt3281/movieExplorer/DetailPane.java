package no.ntnu.imt3281.movieExplorer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DetailPane {

    @FXML
    private Label movieName;

    @FXML
    private ImageView posterImage;

    @FXML
    private Label movieDescription;

    @FXML
    private Label movieGenres;


    /**
     * Used to initialize the detail pane with info on a movie
     * @param item to get info from
     */
    public void init(GUI.SearchResultItem item){
        movieName.setText(item.getTitle());
        JSON movieInfo = Search.movie(item.getId());
        String desc = (String)movieInfo.getValue("overview");
        if(desc == null || desc.trim().equals(""))
            desc = "No Description.";
        movieDescription.setText(desc);

        StringBuilder genreText = new StringBuilder();
        JSON genres = movieInfo.get("genres");
        for(int i = 0; i < genres.size(); i++){
            genreText.append(genres.get(i).getValue("name") + "\n");
        }
        if(genres.size() != 0)
            movieGenres.setText(genreText.toString());
        else
            movieGenres.setText("No genres listed.");


        TheMovieDBConfiguration conf = new TheMovieDBConfiguration(Search.pullData("https://api.themoviedb.org/3/configuration?api_key=" + Search.getAPIKey()));

        String posterPath = conf.getPosterURL(((String)movieInfo.getValue("poster_path")).substring(1));
        posterImage.setImage(new Image(posterPath));

    }

}
