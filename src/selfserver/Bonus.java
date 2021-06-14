/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfserver;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static selfserver.Config.IMAGE_LIFE;

/**
 *
 * @author HP
 */
public class Bonus extends Parent {

    public static final int TYPE_LIFE = 8;

    public static final int COUNT = 9;
    
    public static final String NAMES = "LIFE";
    
    
    private int width;
    private int height;
    private ImageView content;
    
    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    

    public Bonus() {
        content = new ImageView();
        getChildren().add(content);
        
        Image image = Config.getImages().get(IMAGE_LIFE);
        width = (int)(image.getWidth()*0.5);
        height = (int)(image.getHeight()*0.5);
        content.setImage(image);
        setMouseTransparent(true);
    }

    
}
