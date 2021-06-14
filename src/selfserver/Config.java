/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfserver;

import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.util.Duration;

/**
 *
 * @author HP
 */
public final class Config {
    
    public static final Duration ANIMATION_TIME = Duration.millis(40);
    public static final int MAX_LIVES = 9;
    // Screen info
    public static final String IMAGE_DIR = "images/";

    public static final int WINDOW_BORDER = 3; // on desktop platform
    public static final int TITLE_BAR_HEIGHT = 19; // on desktop platform
    public static final int SCREEN_WIDTH = 960;
    public static final int SCREEN_HEIGHT = 700;

    public static final int INFO_TEXT_SPACE = 10;
    
    public static final int FIELD_WIDTH = 700;
    public static final int FIELD_HEIGHT = FIELD_WIDTH;
    public static final int FIELD_Y = SCREEN_HEIGHT - FIELD_HEIGHT;
    
    private static final String[] BOARD_IMAGES = new String[] {
        "3x3.png",
        "4x4.png",
        "5x5.png",
        
    };
    
    private static ObservableList<Image> boardImages = javafx.collections.FXCollections.<Image>observableArrayList();

    public static ObservableList<Image> getBoardImages() {
        return boardImages;
    }
    
    
    public static final int IMAGE_BACKGROUND = 0;
    public static final int IMAGE_GAMEOVER = 1;
    public static final int IMAGE_LOGO = 2;
    public static final int IMAGE_PRESSANYKEY = 3;
    public static final int IMAGE_PRESSANYKEYSHADOW = 4;
    public static final int IMAGE_READY = 5;
    public static final int IMAGE_REDX = 6;
    public static final int IMAGE_YELLOWO = 7;
    public static final int IMAGE_LIFE = 8;
    public static final int IMAGE_TIC = 9;
    public static final int IMAGE_TAC = 10;
    public static final int IMAGE_TOE =11;
    public static final int IMAGE_STRIKE =12;
    public static final int IMAGE_WON =13;
    public static final int IMAGE_LOOSE =14;
    public static final int IMAGE_DRAW =15;

    private static final String[] IMAGES_NAMES = new String[] {
        "background.jpg",
        "gameover.png",
        "logo.png",
        "pressanykey.png",
        "pressanykeyshadow.png",
        "ready.png",
        "redx.png",
        "yellow0.png",
        "life.png",
        "tic.png",
        "tac.png",
        "toe.png",
        "strike.png",
        "won.png",
        "loose.png",
        "draw.png",
    };

    private static ObservableList<Image> images = javafx.collections.FXCollections.<Image>observableArrayList();

    public static ObservableList<Image> getImages() {
        return images;
    }
    
    
    public static void initialize() {
        for (String imageName : IMAGES_NAMES) {
            Image image = new Image(Config.class.getResourceAsStream(IMAGE_DIR+imageName));
            if (image.isError()) {
                System.out.println("Image "+imageName+" not found");
            }
            images.add(image);
        }
        for (String imageName : BOARD_IMAGES) {
            final String url = IMAGE_DIR+imageName;
            Image image = new Image(Config.class.getResourceAsStream(url));
            if (image.isError()) {
                System.out.println("Image "+url+" not found");
            }
            boardImages.add(image);
        }
    }
    
    private Config() {
        
    }

}
