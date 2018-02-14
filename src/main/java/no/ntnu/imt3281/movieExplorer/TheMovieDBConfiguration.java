package no.ntnu.imt3281.movieExplorer;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.prefs.*;

/**
 * Used to get info on image sizes
 */
public class TheMovieDBConfiguration {
    private JSON jsonObject;
    public static Preferences pref = Preferences.userNodeForPackage(TheMovieDBConfiguration.class);


    public TheMovieDBConfiguration(String json) {
        jsonObject = new JSON(json);
        changeImageFolder(pref.get("baseImagePath", "./images"));
    }

    /**
     * Get the url of the backdrop, given an image name
     * @param image_name name of image
     * @return url of backdrop
     */
    public String getBackdropURL(String image_name) {
        return getURL("backdrop", image_name);
    }
    /**
     * Get the url of the logo, given an image name
     * @param image_name name of image
     * @return url of logo
     */
    public String getLogoURL(String image_name) {
        return getURL("logo", image_name);
    }
    /**
     * Get the url of the poster, given an image name
     * @param image_name name of image
     * @return url of poster
     */
    public String getPosterURL(String image_name) {
        return getURL("poster", image_name);
    }
    /**
     * Get the url of the profile, given an image name
     * @param image_name name of image
     * @return url of profile
     */
    public String getProfileURL(String image_name) {
        return getURL("profile", image_name);
    }
    /**
     * Get the url of the still, given an image name
     * @param image_name name of image
     * @return url of still
     */
    public String getStillURL(String image_name) {
        return getURL("still", image_name);
    }

    /**
     * Get the largest image size for an image type
     * @param image_type the image type
     * @return largest size
     */
    private String getLargestSize(String image_type){
        int num_sizes = jsonObject.get("images").get(image_type + "_sizes").size();
        return (String)jsonObject.get("images").get(image_type + "_sizes").getValue(num_sizes - 2);
    }

    /**
     * gets the url of an image
     * @param image_type the type of image (poster, logo, etc..)
     * @param image_name name of the image
     * @return url to the image (local or online)
     */
    private String getURL(String image_type, String image_name){
        File imageFile = new File(pref.get("baseImagePath", "./images") + "/" + image_type + "/" + getLargestSize(image_type) + "/" + image_name);
        String path = "file:" + imageFile.getAbsolutePath();

        // Return the local path if the image exists locally
        // If the image doesn't exist locally, download it to local storage

        if(!imageFile.exists()) {
            String onlineLocation = "http://image.tmdb.org/t/p/" + getLargestSize(image_type) + "/" + image_name;

            try{
                Image image = new Image(onlineLocation);
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null); // From
                ImageIO.write(bufferedImage, "jpg", imageFile);                      // http://www.java2s.com/Tutorials/Java/JavaFX_How_to/Image/Save_an_Image_to_a_file.htm

            } catch (IOException ioe){
                // In the event that the user can't download an image to chosen location for some reason, they will get the online image url
                path = onlineLocation;
                ioe.printStackTrace();
            } catch (RuntimeException re){ // Because the test causes a runtimeException as graphics are not initialized, which is necessary for making an Image()
                path = onlineLocation;
            }
        }
        return path;
    }

    /**
     * Changes setting for base image path and creates folder structure for images at new path
     * @param path the base image folder
     */
    public static void changeImageFolder(String path){
        TheMovieDBConfiguration.pref.put("baseImagePath", path);
        File dir = new File(path);
        if(!dir.exists())
            dir.mkdir();
        for(String type : new String[]{"backdrop", "poster", "profile", "logo", "still"}) {
            dir = new File(path + "/" + type);
            if (!dir.exists())
                dir.mkdir();

            for (String size : new String[]{"w1280", "w500", "w780", "h623", "w300"}) {
                dir = new File(path + "/" + type + "/" + size);
                if (!dir.exists())
                    dir.mkdir();

            }
        }

    }

    /**
     * Clears sub-folders of the image storage location recursively
     * @param path relative path to image storage location. To clear entire cache, send empty string
     */
    public static void clear(String path) {
        File file = new File(pref.get("baseImagePath", "./images") + path);
        for(File child: file.listFiles()){
            if(!child.isDirectory()){
                child.delete();
            }
            else{
                clear(path + "/" + child.getName());
            }
        }
    }
}
