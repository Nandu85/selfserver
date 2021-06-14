/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfserver;

import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import static selfserver.Config.IMAGE_REDX;
import static selfserver.Config.IMAGE_STRIKE;
import static selfserver.Config.IMAGE_YELLOWO;

/**
 *
 * @author HP
 */
public class Strike extends Parent {
    private final ImageView content;
    
    public ImageView getContent(){
        return content;
    }
    
    public void ChangeView(){
        content.setImage(Config.getImages().get(IMAGE_REDX));
        
    }
    
    public void ChangeStrike(){
        content.setImage(Config.getImages().get(IMAGE_STRIKE));
        
    }
    
    public void ChangeO(){
        content.setImage(Config.getImages().get(IMAGE_YELLOWO));
    }
    
    public Strike(){
        content = new ImageView();
        getChildren().add(content);
//        ImageView image = new ImageView();
        content.setImage(Config.getImages().get(IMAGE_STRIKE));
        content.setFitWidth(Config.FIELD_WIDTH/6);
        content.setFitHeight(Config.FIELD_WIDTH/6);
        setMouseTransparent(true);
    }
    
}
