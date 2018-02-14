package no.ntnu.imt3281.movieExplorer;


import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.io.File;
import java.text.DecimalFormat;

public class AboutScreen {
    private Alert alert;

    AboutScreen(){
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("About");
        setContentText();
        alert.setHeaderText(null);

        ButtonType clear_db_btnlk = new ButtonType("Clear Database");
        ButtonType clear_cache_btnlk = new ButtonType("Clear cache");
        ButtonType cancel_btnlk = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(clear_db_btnlk, clear_cache_btnlk, cancel_btnlk);

        // Custom events for clear buttons
        Button clear_db_btn = (Button)alert.getDialogPane().lookupButton(clear_db_btnlk);
        clear_db_btn.addEventFilter(ActionEvent.ACTION, event->{
            event.consume();
            clearDB();
            setContentText();
        });

        Button clear_cache_btn = (Button)alert.getDialogPane().lookupButton(clear_cache_btnlk);
        clear_cache_btn.addEventFilter(ActionEvent.ACTION, event->{
            event.consume();
            clearCache();
            setContentText();
        });

        alert.showAndWait();
    }

    private void clearDB() {
        Search.database.clear();
    }

    private void clearCache() {
        TheMovieDBConfiguration.clear("");
    }

    /**
     * Display the data usage
     */
    private void setContentText(){
        StringBuilder sb = new StringBuilder();

        sb.append("Disc usage:\n");
        sb.append("Database: ");
        long size = getSize(new File("./Database/"));
        sb.append(getReadableSize(size) + "\n");

        String[] names = new String[]{"Poster pictures", "Profile pictures", "Backdrop pictures", "Logo pictures", "Still pictures"};
        String[] paths = new String[]{"poster", "profile", "backdrop", "logo", "still"};
        String basepath = TheMovieDBConfiguration.pref.get("baseImagePath", "./images");
        for(int i = 0; i < names.length; i++){
            sb.append(names[i] + ": ");
            size = getSize(new File(basepath + "/" + paths[i].trim()));
            sb.append(getReadableSize(size) + "\n");
        }

        alert.setContentText(sb.toString());
    }



    /**
     * Get the size of a folder and all children
     * From https://stackoverflow.com/a/25439778
     * @param file file/folder to get the size of
     * @return size
     */
    public static long getSize(File file) {
        long size;
        if (file.isDirectory()) {
            size = 0;
            for (File child : file.listFiles()) {
                size += getSize(child);
            }
        } else {
            size = file.length();
        }
        return size;
    }

    /**
     * Takes a byte value, and converts it into a more readably unit
     * From https://stackoverflow.com/a/25439778
     * @param size byte size
     * @return readable unit
     */
    public static String getReadableSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
