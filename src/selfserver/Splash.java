/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfserver;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author HP
 */
public class Splash extends Parent {
    
    private static final int STATE_SHOW_TITLE = 0;
    private static final int STATE_SUN = 1;
    
    private ImageView background; 
    private ImageView logo;
    private ImageView tic;
    private ImageView tac;
    private ImageView toe;
    private Timeline timeline;
    private int state;
    private int stateArg;
    private ImageView pressanykey;
    private ImageView pressanykeyShadow;
    private ImageView[] NODES;
    private ImageView[] NODES_SHADOWS;
    
    private void initTimeline(){
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame kf = new KeyFrame(Config.ANIMATION_TIME, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (state == STATE_SHOW_TITLE) {
                    stateArg++;
                    int center = Config.SCREEN_WIDTH / 2;
                    int offset = (int)(Math.cos(stateArg / 4.0) * (40 - stateArg) / 40 * center);
                    tic.setTranslateX(center - tic.getImage().getWidth() / 2 + offset);
                    toe.setTranslateX(center - toe.getImage().getWidth() / 2 - offset);
                    tac.setTranslateY(tic.getTranslateY());
                    if (stateArg == 40) {
                        stateArg = 0;
                        state = STATE_SUN;
                    }
                    return;
                }
                if (pressanykey.getOpacity() < 1) {
                    pressanykey.setOpacity(pressanykey.getOpacity() + 0.05f);
                }         
                        
            }      
        
        });
        timeline.getKeyFrames().add(kf);
    }

    void start() {
        background.requestFocus();
        timeline.play();
    }

    void stop() {
        timeline.stop();
    }
    
    Splash() {
        state = STATE_SHOW_TITLE;
        stateArg = 0;
        initTimeline();
        background = new ImageView();
        background.setFocusTraversable(true);
        background.setImage(Config.getImages().get(Config.IMAGE_BACKGROUND));
        background.setFitWidth(Config.SCREEN_WIDTH);
        background.setFitHeight(Config.SCREEN_HEIGHT);
        background.setOnMousePressed(new EventHandler<MouseEvent>() {
            
            public void handle(MouseEvent me) {
                Main.getMainFrame().startGame();
            }
        });
        background.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                Main.getMainFrame().startGame();
            }
        });
        
        tic = new ImageView();
        tic.setImage(Config.getImages().get(Config.IMAGE_TIC));
        tic.setTranslateX(-1000);
        tic.setTranslateY(tic.getImage().getHeight()-5);
        tic.setX(-185);
        
        tac = new ImageView();
        tac.setImage(Config.getImages().get(Config.IMAGE_TAC));
        tac.setTranslateX(0);
        tac.setTranslateY(tic.getImage().getHeight());
        tac.setX(389);
        
        toe = new ImageView();
        toe.setImage(Config.getImages().get(Config.IMAGE_TOE));
        toe.setTranslateX(1000);
        toe.setTranslateY(tic.getImage().getHeight());
        toe.setX(190);
                
        pressanykey = new ImageView();
        pressanykey.setImage(Config.getImages().get(Config.IMAGE_PRESSANYKEY));
        pressanykey.setTranslateX((Config.SCREEN_WIDTH - pressanykey.getImage().getWidth()) / 2);
        double y = tic.getTranslateY() + tic.getImage().getHeight() * 5 / 4;
        pressanykey.setTranslateY(y + (Config.SCREEN_HEIGHT - y) / 2);
        pressanykey.setOpacity(0);
        
        Group group = new Group();
        group.getChildren().add(background);
        group.getChildren().add(tic);
        group.getChildren().add(tac);
        group.getChildren().add(toe);
        group.getChildren().add(pressanykey);
        getChildren().add(group);
    }
    
}
