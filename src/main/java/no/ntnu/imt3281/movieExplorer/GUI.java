package no.ntnu.imt3281.movieExplorer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class GUI {
    @FXML private TextField searchField;
    @FXML private TreeView<SearchResultItem> searchResult;
    @FXML private Pane detailPane;

    
    // Root node for search result tree view
    private TreeItem<SearchResultItem> searchResultRootNode = new TreeItem<SearchResultItem> (new SearchResultItem(""));
    
    /**
     * Called when the object has been created and connected to the fxml file. All components defined in the fxml file is
     * ready and available.
     */
    @FXML
	public void initialize() {
        searchResult.setRoot(searchResultRootNode);
        searchResult.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldVal, newVal) -> onSelect(newVal.getValue(), observable.getValue())
        );
        Search.init();

    }

    /**
     * Called when the seqrch button is pressed or enter is pressed in the searchField.
     * Perform a multiSearch using theMovieDB and add the results to the searchResult tree view.
     *
     * @param event ignored
     */
    @FXML
    void search(ActionEvent event) {
    		JSON result = Search.multiSearch(searchField.getText()).get("results");
    		TreeItem<SearchResultItem> searchResults = new TreeItem<> (new SearchResultItem("Searching for : "+searchField.getText()));
    		searchResultRootNode.getChildren().add(0, searchResults);
    		for (int i=0; i<result.size(); i++) {
    			SearchResultItem item = new SearchResultItem(result.get(i));
    			searchResults.getChildren().add(new TreeItem<SearchResultItem>(item));
    		}
    		searchResultRootNode.setExpanded(true);
    		searchResults.setExpanded(true);
    }

    /**
     * Used when an item in the treeview is selected
     * @param item the searchresultitem
     * @param listItem the item in the treeview
     */
    private void onSelect(SearchResultItem item, TreeItem<SearchResultItem> listItem){
        Platform.runLater(()->{
            // Load children if not already loaded
            if(!item.isChildrenLoaded()){
                item.setChildenLoaded(true);
                if(item.media_type.equals("person")){
                    JSON result = Search.takesPartIn(item.id).get("results");

                    for(int i = 0; i < result.size(); i++){
                        SearchResultItem searchResultItem = new SearchResultItem(result.get(i), "movie");
                        listItem.getChildren().add(new TreeItem<>(searchResultItem));
                    }
                }
                else if (item.media_type.equals("movie") || item.media_type.equals("tv")){
                    JSON result = Search.actors(item.id).get("cast");
                    if(result != null){
                        for(int i = 0; i < result.size(); i++){
                            SearchResultItem searchResultItem = new SearchResultItem(result.get(i), "person");
                            listItem.getChildren().add(new TreeItem<>(searchResultItem));
                        }
                    }
                }
                listItem.setExpanded(true);
            }

            detailPane.getChildren().clear();

            // Display movie info if movie is selected
            if(item.media_type.equals("movie")){
                displayMovie(item);
            }
        });
    }

    /**
     * Loads and displays details about a movie in the detail panel
     * @param item the movie to display info on
     */
    private void displayMovie(SearchResultItem item) {
        Platform.runLater(()->{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailPane.fxml"));
            try{
                detailPane.getChildren().add(loader.load());
                ((DetailPane)loader.getController()).init(item);
            } catch (IOException ioe){
                ioe.printStackTrace();
            }
        });
    }

    /**
     * closes the program
     */
    @FXML
    public void closeApp(){
        Platform.exit();
    }

    /**
     * opens the window for changing image storage location
     */
    @FXML
    public void openPreferences(){
        Stage stage = new Stage();
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Choose new location for storing images");
        dirChooser.setInitialDirectory(new File(TheMovieDBConfiguration.pref.get("baseImagePath", System.getProperty("user.home"))));
        String chosenDir = dirChooser.showDialog(stage).getAbsolutePath();
        TheMovieDBConfiguration.changeImageFolder(chosenDir);
    }

    /**
     * Opens the about window
     */
    @FXML
    public void openAbout(){
        new AboutScreen();
    }



    class SearchResultItem {
    		private String media_type = "";
    		private String name = "";
    		private long id;
    		private String profile_path = "";
    		private String title = "";
    		boolean childrenLoaded = false;
    		
    		/**
    		 * Create new SearchResultItem with the given name as what will be displayed in the tree view.
    		 * 
    		 * @param name the value that will be displayed in the tree view
    		 */
    		public SearchResultItem(String name) {
    			this.name = name;
    		}
    		
    		/**
    		 * Create a new SearchResultItem with data form this JSON object with mediatype embedded in JSON.
    		 * @param json contains the data that will be used to initialize this object.
    		 */
		public SearchResultItem(JSON json) {
		    init(json, (String)json.getValue("media_type"));
		}

        /**
         * Create a new SearchResultItem with data form this JSON.
         * @param json contains the data that will be used to initialize this object.
         * @param mediatype mediatype of the item
         */
		public SearchResultItem(JSON json, String mediatype){
            init(json, mediatype);
        }

        /**
         * Initialize the SearchResultItem with the correct data depending on mediatype
         * @param json contains the data that will be used to initialize this object.
         * @param mediatype mediatype of the item
         */
        private void init(JSON json, String mediatype){
            media_type = mediatype;
            if (media_type.equals("person")) {
                name = (String)json.getValue("name");
                profile_path = (String)json.getValue("profile_path");
            } else if (media_type.equals("movie")) {
                title = (String)json.getValue("title");
            } else if(media_type.equals("tv")){
                name = (String)json.getValue("name");
            }
            id = (Long)json.getValue("id");
        }
    		
		/**
		 * Used by the tree view to get the value to display to the user. 
		 */
		@Override
		public String toString() {
			if (media_type.equals("person")) {
				return name;
			} else if (media_type.equals("movie")) {
				return title;
			} else {
				return name;
			}
		}

        /**
         * Set whether the children of an item has already been loaded
         * @param loaded boolean
         */
		public void setChildenLoaded(boolean loaded){
		    childrenLoaded = loaded;
        }

        /**
         *
         * @return has the children of this item been loaded?
         */
        public boolean isChildrenLoaded() {
            return childrenLoaded;
        }

        /**
         * get title if the item
         * @return title of item
         */
        public String getTitle(){
		    return title;
        }

        /**
         * get id of the item
         * @return id of item
         */
        public long getId() {
            return id;
        }
    }
}
