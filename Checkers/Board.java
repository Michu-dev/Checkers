
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;



public class Board extends Application implements BeatOrKing{

    public static final int field_size = 100;
    private Group fieldGroup = new Group();
    private Group checkerGroup = new Group();
    private Field[][] setup = new Field[8][8]; // set of the board

    private boolean turn = true; //true - white pieces, false - black pieces


    //logic of kills
    public boolean isBeatenOrKing(CheckerType color){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(setup[j][i].hasChecker() && setup[j][i].getChecker().getType() == color){
                    if(j - 2 >= 0 && i - 2 >= 0){
                        if(setup[j - 1][i - 1].hasChecker() && setup[j - 1][i - 1].getChecker().getType() != color && !setup[j - 2][i - 2].hasChecker())
                            return true;
                    }
                    if(j - 2 >= 0 && i + 2 < 8){
                        if(setup[j - 1][i + 1].hasChecker() && setup[j - 1][i + 1].getChecker().getType() != color && !setup[j - 2][i + 2].hasChecker())
                            return true;
                    }
                    if(j + 2 < 8 && i - 2 >= 0){
                        if(setup[j + 1][i - 1].hasChecker() && setup[j + 1][i - 1].getChecker().getType() != color && !setup[j + 2][i - 2].hasChecker())
                            return true;
                    }
                    if(j + 2 < 8 && i + 2 < 8){
                        if(setup[j + 1][i + 1].hasChecker() && setup[j + 1][i + 1].getChecker().getType() != color && !setup[j + 2][i + 2].hasChecker())
                            return true;
                    }
                }
            }
        }
        return false;
    }
    //set all elements in the window
    public Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(800, 800);
        root.getChildren().addAll(fieldGroup, checkerGroup);

        
        
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                
                Field field = new Field(j, i, (i+j) % 2 == 0);
                setup[j][i] = field;
                fieldGroup.getChildren().add(field);
                Checker p = null;
                if(i <= 2 && (i+j) % 2 != 0) {
                    p = createCheckers(CheckerType.BLACK, j, i);
                }
                if(i >= 5 && (i+j) % 2 != 0) {
                    p = createCheckers(CheckerType.WHITE, j, i);
                }
                if(p != null){
                    field.setChecker(p);
                    checkerGroup.getChildren().add(p);
                }
                
            }
        }

        return root;

    }
    //pixel numbers to coordinate axis
    public int toSetup(double pixel){
        return (int)(pixel + field_size / 2) / field_size;
    }

    public MvDone tryMv(Checker checker, int newX, int newY){
        if(setup[newX][newY].hasChecker() || (newX + newY) % 2 == 0){ // white fields or pola with pieces different kinds
            return new MvDone(MvType.NOTHING);
        }
        int xP = toSetup(checker.getOldX());
        int yP = toSetup(checker.getOldY());

        

        if((Math.abs(newX - xP) == 1 && newY - yP == checker.getType().mvDirection) || (Math.abs(newX - xP) == 1 && checker.isBeatenOrKing(checker.getType()))){ //normalny ruch do przodu pionka
            return new MvDone(MvType.NORMAL);
        } else if(Math.abs(newX - xP) == 2 && Math.abs(newY - yP) == Math.abs(checker.getType().mvDirection * 2) || checker.isBeatenOrKing(checker.getType())){ //bicie
            //współrzędne bitego pionka
            int xK1 = xP + (newX - xP) / 2;
            int yK1 = yP + (newY - yP) / 2;
            if(setup[xK1][yK1].hasChecker() && setup[xK1][yK1].getChecker().getType() != checker.getType()){
                return new MvDone(MvType.BEAT, setup[xK1][yK1].getChecker());
            }

        }
        return new MvDone(MvType.NOTHING);



    }

    



    

    public Checker createCheckers(CheckerType type, int x, int y){
        Checker checker = new Checker(type, x, y);
        checker.setOnMouseReleased(c -> {
            int x1 = toSetup(checker.getLayoutX());
            int y1 = toSetup(checker.getLayoutY());

            MvDone effect = tryMv(checker, x1, y1);
            CheckerType color = checker.getType();
            int oldX1 = toSetup(checker.getOldX());
            int oldY1 = toSetup(checker.getOldY());
            boolean beat = isBeatenOrKing(color);
            if(turn){
                
                if(color == CheckerType.WHITE){
                    if(beat){
                        switch(effect.getType()){
                            case BEAT:
                                checker.mv(x1, y1);
                                setup[oldX1][oldY1].setChecker(null);
                                setup[x1][y1].setChecker(checker);
                                Checker beatenChecker = effect.getChecker();
                                setup[toSetup(beatenChecker.getOldX())][toSetup(beatenChecker.getOldY())].setChecker(null);
                                checkerGroup.getChildren().remove(beatenChecker);
                                if(!isBeatenOrKing(color))
                                    turn = false; //next move black pieces
                                break;
                            default:
                                checker.blockMv();
                                break;
                        }

                    } else {
                        switch(effect.getType()){
                            case NOTHING:
                                checker.blockMv();
                                break;
                            case NORMAL:
                                checker.mv(x1, y1);
                                setup[oldX1][oldY1].setChecker(null);
                                setup[x1][y1].setChecker(checker);
                                turn = false; //next move black pieces
                                break;
                            case BEAT:
                                checker.mv(x1, y1);
                                setup[oldX1][oldY1].setChecker(null);
                                setup[x1][y1].setChecker(checker);
                                Checker beatenChecker = effect.getChecker();
                                setup[toSetup(beatenChecker.getOldX())][toSetup(beatenChecker.getOldY())].setChecker(null);
                                checkerGroup.getChildren().remove(beatenChecker);
                                turn = false; //next move black pieces
                                break;
                        
                        }
                        
                    }
                    if(y1 == 0) checker.setKing(); //king
                     
                } else {
                    checker.blockMv();
                    
                }
                
            } else {
                if(color == CheckerType.BLACK){
                    if(beat){
                        switch(effect.getType()){
                            case BEAT:
                                checker.mv(x1, y1);
                                setup[oldX1][oldY1].setChecker(null);
                                setup[x1][y1].setChecker(checker);
                                Checker beatenChecker = effect.getChecker();
                                setup[toSetup(beatenChecker.getOldX())][toSetup(beatenChecker.getOldY())].setChecker(null);
                                checkerGroup.getChildren().remove(beatenChecker);
                                if(!isBeatenOrKing(color))
                                    turn = true; //next move white pieces
                                break;
                            default:
                                checker.blockMv();
                                break;
                        }
                        
                    } else {
                        switch(effect.getType()){
                            case NOTHING:
                                checker.blockMv();
                                break;
                            case NORMAL:
                                checker.mv(x1, y1);
                                setup[oldX1][oldY1].setChecker(null);
                                setup[x1][y1].setChecker(checker);
                                turn = true; //next move white pieces
                                break;
                            case BEAT:
                                checker.mv(x1, y1);
                                setup[oldX1][oldY1].setChecker(null);
                                setup[x1][y1].setChecker(checker);
                                Checker beatenChecker = effect.getChecker();
                                setup[toSetup(beatenChecker.getOldX())][toSetup(beatenChecker.getOldY())].setChecker(null);
                                checkerGroup.getChildren().remove(beatenChecker);
                                turn = true; //next move white pieces
                                break;
                        }
                        
                    }
                    
                    if(y1 == 7) checker.setKing(); //king
                } else {
                    checker.blockMv();
                    
                }
                
            }
            
            

        });
        return checker;
    }


    public static void main(String[] args){
        launch(args);
    }
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Checkers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }



}