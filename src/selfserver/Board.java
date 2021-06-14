/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfserver;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author HP
 */
public class Board extends Parent {

    private final int TYPE_3 = 0;
    private final int TYPE_4 = 1;
    private final int TYPE_5 = 2;
    
    private int type;
    private ImageView content;
    
    public Board() {
        content = new ImageView();
        getChildren().add(content);
        type=getType();
        changeType(type);
        setMouseTransparent(true);
    }
        
    public int getType() {
        return type;
    }
    
    public void changeType(int newType) {
        this.type = newType;
        Image image = Config.getBoardImages().get(type);
        content.setImage(image);
        content.setFitWidth(Config.FIELD_WIDTH);
        content.setFitHeight(Config.FIELD_WIDTH);
    }
    
    
    public int getBoardType(int s) {
        switch (s) {
            case 5:
                return TYPE_5;
            case 4:
                return TYPE_4;
            default:
                return TYPE_3;
        }
        
    }
    
    
}
