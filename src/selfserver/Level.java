/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfserver;

import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import static selfserver.Config.IMAGE_DRAW;
import static selfserver.Config.IMAGE_GAMEOVER;
import static selfserver.Config.IMAGE_LOOSE;
import static selfserver.Config.IMAGE_WON;
import selfserver.Main.MainFrame;

/**
 *
 * @author HP
 */
public class Level extends Parent {
    
    private static final double MOB_SCALING = 1.5f;
    private static final MainFrame mainFrame = Main.getMainFrame();
    
    private Group group;
    private ArrayList<Board> boards;
    private ArrayList<Bonus> lives;
    
    
    // States
    // 0 - starting level
    // 1 - ball is catched
    // 2 - playing
    // 3 - game over
    private static final int STARTING_LEVEL = 0;
    private static final int PLAYING = 1;
    private static final int YOU_WON = 2;
    private static final int YOU_LOSE = 3;
    private static final int MATCH_DRAW = 4;
    private static final int GAME_OVER = 5;
    
    private ArrayList XOs;   //To check winning condition of X
    //To check winning condition of O  //Create function wincheck for it
    //0--null
    //1--user
    //2-computer
    private int turn;
    ImageView won=new ImageView();
    private int state;
    private Board board;
    private ArrayList<Strike> strike;
    private int levelNumber=1;
    private Text roundCaption;
    private Text round;
    private Text scoreCaption;
    private Text score;
    private Text livesCaption;
    private ImageView message;
    private Timeline startingTimeline;
    private Timeline timeline;
    private Group infoPanel;
    
    public Level(int state) {
        group = new Group();
        getChildren().add(group);
        initContent(levelNumber);
        
    }
    
    private void initStartingTimeline() {
        if(group.getChildren().contains(won))
            group.getChildren().remove(won);
        startingTimeline = new Timeline();
        KeyFrame kf1 = new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                message.setVisible(true);
                state = STARTING_LEVEL;
                board.setVisible(false);
                for(Strike s:strike){
                    s.setVisible(false);
                }
                
            }
        }, new KeyValue(message.opacityProperty(), 0));
        KeyFrame kf2 = new KeyFrame(Duration.millis(1500), new KeyValue(message.opacityProperty(), 1));
        KeyFrame kf3 = new KeyFrame(Duration.millis(3000), new KeyValue(message.opacityProperty(), 1));
        KeyFrame kf4 = new KeyFrame(Duration.millis(4000), new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                message.setVisible(false);
                for(Strike s:strike){
                    s.setVisible(true);
                }         
                board.setVisible(true);
                
                state = PLAYING;
                turn=0;
            }
        }, new KeyValue(message.opacityProperty(), 0));

        startingTimeline.getKeyFrames().addAll(kf1, kf2, kf3, kf4);
    }

    

    void stop() {
        startingTimeline.stop();
        timeline.stop();
    }


    private void updateScore(int inc) {
        mainFrame.setScore(mainFrame.getScore() + inc);
        score.setText(mainFrame.getScore() + "");
        round.setText(levelNumber+"");
    }

    private void updateLives() {
        while (lives.size() > mainFrame.getLifeCount()) {
            Bonus lifeBat = lives.get(lives.size() - 1);
            lives.remove(lifeBat);
            infoPanel.getChildren().remove(lifeBat);
        }
        // Add lifes (but no more than 9)
        int maxVisibleLifes = 9;
        double scale = 0.2;

        for (int life = lives.size(); life < Math.min(mainFrame.getLifeCount(), maxVisibleLifes); life++) {
            Bonus lifeBonus = new Bonus();
            lifeBonus.setScaleX(scale);
            lifeBonus.setScaleY(scale);
            lifeBonus.setTranslateX(livesCaption.getTranslateX()+(life%3)*50-120);
            lifeBonus.setTranslateY(livesCaption.getTranslateY() +
                (life / 3)*45-115);
            lives.add(lifeBonus);
            infoPanel.getChildren().add(lifeBonus);
        }
    }
    
    private void winCheck(){
    int row,col,i,j;
        for(col=0;col<levelNumber+2;col++){
            for(row=0;row<levelNumber+2;row++){
                
                if(row<levelNumber){
                j=1;
                i=j;
                if((XOs.get(row+(levelNumber+2)*col).equals(j) && XOs.get(row+(levelNumber+2)*col+1).equals(j) && XOs.get(row+(levelNumber+2)*col+2).equals(i)))
                    state=YOU_WON;
                j=2;
                i=j;
                if((XOs.get(row+(levelNumber+2)*col).equals(j) && XOs.get(row+(levelNumber+2)*col+1).equals(j) && XOs.get(row+(levelNumber+2)*col+2).equals(i)))
                    state=YOU_LOSE;    
                
                }
                
                if(col<levelNumber){
                    j=1;
                    i=j;
                    if((XOs.get((levelNumber+2)*col+row).equals(j) && XOs.get((levelNumber+2)*col+row+(levelNumber+2)).equals(j) && XOs.get((levelNumber+2)*col+row+2*(levelNumber+2)).equals(i)))
                        state=YOU_WON;
                    j=2;
                    i=j;
                    if((XOs.get((levelNumber+2)*col+row).equals(j) && XOs.get((levelNumber+2)*col+row+(levelNumber+2)).equals(j) && XOs.get((levelNumber+2)*col+row+(levelNumber+2)*2).equals(i)))
                        state=YOU_LOSE;
                }
                if(row<levelNumber && col<levelNumber){
                    
                    j=1;
                    if(XOs.get(row+(levelNumber+2)*col).equals(j) && XOs.get(row+(levelNumber+2)*col+(levelNumber+3)).equals(j) && XOs.get(row+(levelNumber+2)*col+(levelNumber+3)*2).equals(j))
                        state=YOU_WON;
                    j=2;
                    if(XOs.get(row+(levelNumber+2)*col).equals(j) && XOs.get(row+(levelNumber+2)*col+(levelNumber+3)).equals(j) && XOs.get(row+(levelNumber+2)*col+(levelNumber+3)*2).equals(j))
                        state=YOU_LOSE;
                }
                if(row>1 && col<levelNumber){
                    
                    j=1;
                    if(XOs.get(row+(levelNumber+2)*col).equals(j) && XOs.get(row+(levelNumber+2)*col+(levelNumber+1)).equals(j) && XOs.get(row+(levelNumber+2)*col+(levelNumber+1)*2).equals(j))
                        state=YOU_WON;
                    j=2;
                    if(XOs.get(row+(levelNumber+2)*col).equals(j) && XOs.get(row+(levelNumber+2)*col+(levelNumber+1)).equals(j) && XOs.get(row+(levelNumber+2)*col+(levelNumber+1)*2).equals(j))
                        state=YOU_LOSE;
                }
                System.out.println("Win/////Lose");
                
            }
        }    
     }
    
    private void initInfoPanel(){
        infoPanel = new Group();
        roundCaption = new Text();
        roundCaption.setText("ROUND");
        roundCaption.setTextOrigin(VPos.TOP);
        roundCaption.setFill(Color.rgb(51, 102, 51));
        Font f = new Font("Impact", 18);
        roundCaption.setFont(f);
        roundCaption.setTranslateX(30);
        roundCaption.setTranslateY(128);
        round = new Text();
        round.setTranslateX(roundCaption.getTranslateX() +
            roundCaption.getBoundsInLocal().getWidth() + Config.INFO_TEXT_SPACE);
        round.setTranslateY(roundCaption.getTranslateY());
        round.setText(levelNumber + "");
        round.setTextOrigin(VPos.TOP);
        round.setFont(f);
        round.setFill(Color.rgb(0, 204, 102));
        
        scoreCaption = new Text();
        scoreCaption.setText("SCORE");
        scoreCaption.setFill(Color.rgb(51, 102, 51));
        scoreCaption.setTranslateX(30);
        scoreCaption.setTranslateY(170);
        scoreCaption.setTextOrigin(VPos.TOP);
        scoreCaption.setFont(f);
        
        score = new Text();
        
        score.setTranslateX(scoreCaption.getTranslateX() +
        scoreCaption.getBoundsInLocal().getWidth() + Config.INFO_TEXT_SPACE);
        score.setTranslateY(scoreCaption.getTranslateY());
        score.setFill(Color.rgb(0, 204, 102));
        score.setTextOrigin(VPos.TOP);
        score.setFont(f);
        score.setText("");
        
        livesCaption = new Text();
        livesCaption.setText("LIFE");
        livesCaption.setTranslateX(30);
        livesCaption.setTranslateY(225);
        livesCaption.setFill(Color.rgb(51, 102, 51));
        livesCaption.setTextOrigin(VPos.TOP);
        livesCaption.setFont(f);
        
        Button newGame= new Button();
        newGame.setText("New Game");
        newGame.setTranslateX(50);
        newGame.setTranslateY(350);
        newGame.setOnAction(new EventHandler<ActionEvent>() {
            
                @Override
                public void handle(ActionEvent ae) {
                    state=STARTING_LEVEL;
                    start();
                    for(Strike s:strike){
                        s.ChangeStrike();
                    }
                    int j=0;
                    for(Object i:XOs){
                        XOs.set(j, 0);
                        j++;
                        System.out.println(XOs);
                    }
                }            
        });
       
        
        int infoWidth = Config.SCREEN_WIDTH - Config.FIELD_WIDTH;
        Rectangle black = new Rectangle();
        black.setWidth(infoWidth);
        black.setHeight(Config.SCREEN_HEIGHT);
        black.setFill(Color.BLACK);
        ImageView verLine = new ImageView();
        verLine.setImage(new Image(Level.class.getResourceAsStream(Config.IMAGE_DIR+"vline.png")));
        verLine.setTranslateX(3);
        ImageView logo = new ImageView();
        logo.setImage(Config.getImages().get(Config.IMAGE_LOGO));
        logo.setTranslateX(20);
        logo.setTranslateY(30);
        
        
        
        infoPanel.getChildren().addAll(black, verLine, logo, roundCaption,
                round, scoreCaption, score, livesCaption, newGame);
        infoPanel.setTranslateX(Config.FIELD_WIDTH);
    }
    
    private void initContent(int level){
        
        
        lives = new ArrayList<>();
        strike = new ArrayList<>();
        XOs = new ArrayList<>();
        
        board = new Board();
        board.changeType(levelNumber-1);
        board.setVisible(false);
        for(Strike s:strike){
                    s.setVisible(false);
                }
        
        message = new ImageView();
        message.setImage(Config.getImages().get(Config.IMAGE_READY));
        message.setTranslateX((Config.FIELD_WIDTH - message.getImage().getWidth()) / 2);
        message.setTranslateY(Config.FIELD_Y +
            (Config.FIELD_HEIGHT - message.getImage().getHeight()) / 2);
        message.setVisible(false);
        initLevel();
        initStartingTimeline();
        initTimeline();
        initInfoPanel();
        ImageView background = new ImageView();
        background.setFocusTraversable(true);
        background.setImage(Config.getImages().get(Config.IMAGE_BACKGROUND));
        background.setFitWidth(Config.SCREEN_WIDTH);
        background.setFitHeight(Config.SCREEN_HEIGHT);
        background.setOnMouseClicked(new EventHandler<MouseEvent>() {
            
                @Override
                public void handle(MouseEvent me) {
                    int count=(int)(me.getX()*(levelNumber+2)/700)+(levelNumber+2)*(int)(me.getY()*(levelNumber+2)/700);
                    if(turn==0 && state==PLAYING){
                        if(XOs.get(count).equals(0)){
                            strike.get(count).ChangeView();
                            XOs.set(count, 1);
                            turn=1;
                            winCheck();           
                            
                       }

                    }
                    System.out.println(me.getX()+"  "+me.getY());
                    System.out.println(count);
                    System.out.println(turn);
                    System.out.println("????????");
                    initTimeline();
                }
            });
        group.getChildren().add(background);
        
        group.getChildren().addAll(message, board, infoPanel);
        
        group.getChildren().addAll(strike);
        
        if(group.getChildren().contains(won))
            group.getChildren().remove(won);
        
        state = STARTING_LEVEL;
        
    }

    private void initTimeline() {
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame kf = new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(state==PLAYING){
                    if(turn==1){
                        int position=getpos();
                            if(position!=-1){
                                System.out.println(position);
                                strike.get(position).ChangeO();
                                XOs.set(position, 2);
                                winCheck();
                                turn=0;
                            }
                            else
                                state=MATCH_DRAW;
                        //System.out.print(position);
                        //System.out.println("XOs.get(position); "+XOs.get(position));
                    }                               
                }
                else if(state==YOU_WON){
                    if(group.getChildren().contains(won))
                        group.getChildren().remove(won);
                    won.setImage(Config.getImages().get(IMAGE_WON));
                    won.setTranslateX(150);
                    won.setTranslateY(200);
                    won.setScaleX(1.5);
                    won.setScaleY(1.5);
                    group.getChildren().add(won);
//                    won.setOpacity(1);
//                    won.setVisible(true);
//                    won.requestFocus();
//                    
//                    state=STARTING_LEVEL;
                    System.out.println("YOUWIN");
                    if(levelNumber<3)
                        levelNumber++;
                    updateScore(100);
                    
                    
//                    initContent(levelNumber);
                    
                }
                else if(state==YOU_LOSE){
                    if(group.getChildren().contains(won))
                        group.getChildren().remove(won);
                    won.setImage(Config.getImages().get(IMAGE_LOOSE));
                    won.setTranslateX(150);
                    won.setTranslateY(200);
                    won.setScaleX(1.5);
                    won.setScaleY(1.5);
                    group.getChildren().add(won);
                    updateScore(-30);
                    System.out.println("YOULOSE");
                    Main.getMainFrame().decreaseLives();
                    if(lives.size()==1)
                        state=GAME_OVER;
                    else
                        state=STARTING_LEVEL;
                }
                else if(state==MATCH_DRAW){
                    if(group.getChildren().contains(won))
                        group.getChildren().remove(won);
                    won.setImage(Config.getImages().get(IMAGE_DRAW));
                    won.setTranslateX(50);
                    won.setTranslateY(160);
                    won.setScaleX(1.5);
                    won.setScaleY(1.5);
                    group.getChildren().add(won);
                    state=STARTING_LEVEL;
                    System.out.println("DRAW");
                    //levelNumber++;
                    updateScore(50);
                }
                else if(state==STARTING_LEVEL){
                    group.getChildren().remove(won);
                   
                    for(Strike s:strike){
                            s.ChangeStrike();
                        }
                        int j=0;
                        for(Object i:XOs){
                            XOs.set(j, 0);
                            j++;
                            System.out.println(XOs);
                        }
                        //initLevel();
                        
                        start();
                }
                else if(state==GAME_OVER){
                    if(group.getChildren().contains(won))
                        group.getChildren().remove(won);
                    won.setImage(Config.getImages().get(IMAGE_GAMEOVER));
                    won.setTranslateX(150);
                    won.setTranslateY(250);
                    won.setScaleX(1.5);
                    won.setScaleY(1.5);
                    group.getChildren().add(won);
                    Main.getMainFrame().changeState(0);
                }
            
            }
        });
        KeyFrame kf1 = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(state==YOU_WON){
//                    group.getChildren().add(won);
                    board.changeType(levelNumber-1);
                    group.getChildren().removeAll(strike);
                    initContent(levelNumber);
                    
                }
                
            }
            
            });
        
        timeline.getKeyFrames().addAll(kf,kf1);
    }
    
    private void initLevel(){             
        System.out.println(levelNumber);
//        strike.clear();
//        XOs.clear();
        if(levelNumber==1){
            for(int row=0;row<levelNumber+2;row++){
                for(int col=0;col<levelNumber+2;col++){
                    Strike s=new Strike();
                    s.setTranslateX(col*226+50);
                    s.setTranslateY(row*218+50);
                    strike.add(s);
                    int k=0;
                    XOs.add(k);
                }
            }
        }
        else if(levelNumber==2){
            System.out.println(strike);
            
            strike.clear();
            XOs.clear();
            group.getChildren().removeAll(strike);
            for(int row=0;row<levelNumber+2;row++){
                for(int col=0;col<levelNumber+2;col++){
                    Strike s=new Strike();
                    s.setTranslateX(col*175+20);
                    s.setTranslateY(row*175+20);
                    strike.add(s);
                    int k=0;
                    XOs.add(k);
                }
            }
            //group.getChildren().addAll(strike);
        }
        else if(levelNumber==3){
            System.out.println(strike);
            strike.clear();
            XOs.clear();
            group.getChildren().removeAll(strike);
            for(int row=0;row<levelNumber+2;row++){
                for(int col=0;col<levelNumber+2;col++){
                    Strike s=new Strike();
                    s.setTranslateX(col*140+10);
                    s.setTranslateY(row*140+10);
                    strike.add(s);
                    int k=0;
                    XOs.add(k);
                }
            }
            //group.getChildren().addAll(strike);
        }
    }
    
    private int getpos(){
        int row,col,coll,i=0,j=2;
        for(col=0;col<levelNumber+2;col++){
            for(row=0;row<levelNumber+2;row++){
                
                //position if computer can win
                if(row<=levelNumber-1){
                    if(XOs.get(row+(levelNumber+2)*col).equals(j) && XOs.get(row+(levelNumber+2)*col+1).equals(j) && XOs.get(row+(levelNumber+2)*col+2).equals(i))
                        return row+(levelNumber+2)*col+2;
                    if(XOs.get(row+(levelNumber+2)*col+2).equals(j) && XOs.get(row+(levelNumber+2)*col+1).equals(j) && XOs.get(row+(levelNumber+2)*col).equals(i))
                        return row+(levelNumber+2)*col;
                    if(XOs.get(row+(levelNumber+2)*col).equals(j) && XOs.get(row+(levelNumber+2)*col+2).equals(j) && XOs.get(row+(levelNumber+2)*col+1).equals(i))
                        return row+(levelNumber+2)*col+1;
                }
               // System.out.println("............");
                if(col<levelNumber){
                    if(XOs.get((levelNumber+2)*col+row).equals(j) && XOs.get((levelNumber+2)*col+row+(levelNumber+2)).equals(j) && XOs.get((levelNumber+2)*col+row+(levelNumber+2)*2).equals(i))
                        return (levelNumber+2)*col+row+(levelNumber+2)*2;
                    if(XOs.get((levelNumber+2)*col+row+(levelNumber+2)*2).equals(j) && XOs.get((levelNumber+2)*col+row+(levelNumber+2)).equals(j) && XOs.get((levelNumber+2)*col+row).equals(i))
                        return (levelNumber+2)*col+row;
                    if(XOs.get((levelNumber+2)*col+row).equals(j) && XOs.get((levelNumber+2)*col+row+2*(levelNumber+2)).equals(j) && XOs.get((levelNumber+2)*col+row+(levelNumber+2)).equals(i))
                        return (levelNumber+2)*col+row+(levelNumber+2);
                }
                //System.out.println("////////////////");
                if(row<levelNumber && col<levelNumber){                  
                    if(XOs.get(row+(levelNumber+2)*col).equals(j) && XOs.get(row+(levelNumber+2)*col+(levelNumber+3)).equals(j) && XOs.get(row+(levelNumber+2)*col+(levelNumber+3)*2).equals(i))
                        return row+(levelNumber+2)*col+2*(levelNumber+3);
                    if(XOs.get(row+(levelNumber+2)*col+2*(levelNumber+3)).equals(j) && XOs.get(row+(levelNumber+2)*col+(levelNumber+3)).equals(j) && XOs.get(row+(levelNumber+2)*col).equals(i))
                        return row+(levelNumber+2)*col;
                    if(XOs.get(row+(levelNumber+2)*col).equals(j) && XOs.get(row+(levelNumber+2)*col+2*(levelNumber+3)).equals(j) && XOs.get(row+(levelNumber+2)*col+(levelNumber+3)).equals(i))
                        return row+(levelNumber+2)*col+(levelNumber+3);
                }
                if(row>1 && col<levelNumber){                  
                    if(XOs.get(row+(levelNumber+2)*col).equals(j) && XOs.get(row+(levelNumber+2)*col+(levelNumber+1)).equals(j) && XOs.get(row+(levelNumber+2)*col+2*(levelNumber+1)).equals(i))
                        return row+(levelNumber+2)*col+2*(levelNumber+1);
                    if(XOs.get(row+(levelNumber+2)*col+2*(levelNumber+1)).equals(j) && XOs.get(row+(levelNumber+2)*col+(levelNumber+1)).equals(j) && XOs.get(row+(levelNumber+2)*col).equals(i))
                        return row+(levelNumber+2)*col;
                    if(XOs.get(row+(levelNumber+2)*col).equals(j) && XOs.get(row+(levelNumber+2)*col+2*(levelNumber+1)).equals(j) && XOs.get(row+(levelNumber+2)*col+(levelNumber+1)).equals(i))
                        return row+(levelNumber+2)*col+(levelNumber+1);
                }
            }
        }
        for(col=0;col<levelNumber+2;col++){
            for(row=0;row<levelNumber+2;row++){
                //if computer could stop you
                i=0;
                j=1;
                System.out.println(row+" "+col);
//                if(row+(levelNumber+2)*col+2>levelNumber*levelNumber-1)
//                    continue;
                if(row<=levelNumber-1){
                    if(XOs.get(row+(levelNumber+2)*col).equals(j) && XOs.get(row+(levelNumber+2)*col+1).equals(j) && XOs.get(row+(levelNumber+2)*col+2).equals(i))
                        return row+(levelNumber+2)*col+2;
                    if(XOs.get(row+(levelNumber+2)*col+2).equals(j) && XOs.get(row+(levelNumber+2)*col+1).equals(j) && XOs.get(row+(levelNumber+2)*col).equals(i))
                        return row+(levelNumber+2)*col;
                    if(XOs.get(row+(levelNumber+2)*col).equals(j) && XOs.get(row+(levelNumber+2)*col+2).equals(j) && XOs.get(row+(levelNumber+2)*col+1).equals(i))
                        return row+(levelNumber+2)*col+1;
                }
                //System.out.println("............");
                if(col<levelNumber){
                    if(XOs.get((levelNumber+2)*col+row).equals(j) && XOs.get((levelNumber+2)*col+row+(levelNumber+2)).equals(j) && XOs.get((levelNumber+2)*col+row+(levelNumber+2)*2).equals(i))
                        return (levelNumber+2)*col+row+(levelNumber+2)*2;
                    if(XOs.get((levelNumber+2)*col+row+(levelNumber+2)*2).equals(j) && XOs.get((levelNumber+2)*col+row+(levelNumber+2)).equals(j) && XOs.get((levelNumber+2)*col+row).equals(i))
                        return (levelNumber+2)*col+row;
                    if(XOs.get((levelNumber+2)*col+row).equals(j) && XOs.get((levelNumber+2)*col+row+2*(levelNumber+2)).equals(j) && XOs.get((levelNumber+2)*col+row+(levelNumber+2)).equals(i))
                        return (levelNumber+2)*col+row+(levelNumber+2);
                }
                //System.out.println("////////////////");
                if(row<levelNumber && col<levelNumber){                  
                    if(XOs.get(row+(levelNumber+2)*col).equals(j) && XOs.get(row+(levelNumber+2)*col+(levelNumber+3)).equals(j) && XOs.get(row+(levelNumber+2)*col+2*(levelNumber+3)).equals(i))
                        return row+(levelNumber+2)*col+(levelNumber+3)*2;
                    if(XOs.get(row+(levelNumber+2)*col+(levelNumber+3)*2).equals(j) && XOs.get(row+(levelNumber+2)*col+(levelNumber+3)).equals(j) && XOs.get(row+(levelNumber+2)*col).equals(i))
                        return row+(levelNumber+2)*col;
                    if(XOs.get(row+(levelNumber+2)*col).equals(j) && XOs.get(row+(levelNumber+2)*col+2*(levelNumber+3)).equals(j) && XOs.get(row+(levelNumber+2)*col+(levelNumber+3)).equals(i))
                        return row+(levelNumber+2)*col+(levelNumber+3);
                }
                if(row>1 && col<levelNumber){                  
                    if(XOs.get(row+(levelNumber+2)*col).equals(j) && XOs.get(row+(levelNumber+2)*col+(levelNumber+1)).equals(j) && XOs.get(row+(levelNumber+2)*col+2*(levelNumber+1)).equals(i))
                        return row+(levelNumber+2)*col+2*(levelNumber+1);
                    if(XOs.get(row+(levelNumber+2)*col+(levelNumber+1)*2).equals(j) && XOs.get(row+(levelNumber+2)*col+(levelNumber+1)).equals(j) && XOs.get(row+(levelNumber+2)*col).equals(i))
                        return row+(levelNumber+2)*col;
                    if(XOs.get(row+(levelNumber+2)*col).equals(j) && XOs.get(row+(levelNumber+2)*col+2*(levelNumber+1)).equals(j) && XOs.get(row+(levelNumber+2)*col+(levelNumber+1)).equals(i))
                        return row+(levelNumber+2)*col+(levelNumber+1);
                }
            }
        }
                //return center if available
                i=0;
                if(levelNumber%2==1 && XOs.get((int)(((levelNumber+2)*(levelNumber+2)-1)/2)).equals(i))
                    return (int)(((levelNumber+2)*(levelNumber+2)-1)/2);
                if(levelNumber==2){
                    if(XOs.get(5).equals(i))
                        return 5;
                    if(XOs.get(9).equals(i))
                        return 9;
                    if(XOs.get(6).equals(i))
                        return 6;
                    if(XOs.get(10).equals(i))
                        return 10;
                
                }
                //return if corners available
                i=0;
                if(XOs.get(0).equals(i))
                    return 0;
                if(XOs.get(levelNumber+1).equals(i))
                    return levelNumber+1;
                if(XOs.get((levelNumber+2)*(levelNumber+2)-levelNumber-2).equals(i))
                    return (levelNumber+2)*(levelNumber+2)-levelNumber-2;
                if(XOs.get((levelNumber+2)*(levelNumber+2)-1).equals(i))
                    return (levelNumber+2)*(levelNumber+2)-1;
                //return the first empty position
                int x=0;
                for(Object k:XOs){
                    if(k.equals(0))
                        return x;
                    x++;
                }                
        return -1;
    }
    
    void start() {
        startingTimeline.play();
        timeline.play();
        group.getChildren().get(0).requestFocus();
        updateScore(0);
        updateLives();
    }

}

