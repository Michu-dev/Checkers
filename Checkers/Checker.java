import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class Checker extends StackPane implements BeatOrKing {
    private CheckerType type;

    private double clickX, clickY;
    private double oldX, oldY; 
    private boolean king = false;

    public void setKing(){
        king = true;
    }

    public boolean isBeatenOrKing(CheckerType type){
        return king;
    }

    public CheckerType getType() {
        return type;
    } 
    public Checker(CheckerType type, int x, int y){
        mv(x, y);
        this.type = type;
        Ellipse shape = new Ellipse(Board.field_size * 0.3125, Board.field_size * 0.26); //fit shape of a piece to the field of the game
        
        /*if((type == CheckerType.BLACK && y == 7) || king){
            shape.setFill(Color.valueOf("#A52A2A")); //color dark king
            shape.setStroke(Color.BLACK);//kolor zewnetrznych krawedzi
            shape.setStrokeWidth(Board.field_size * 0.03);

            shape.setTranslateX((Board.field_size - Board.field_size * 0.3125 * 2) / 2);
            shape.setTranslateY((Board.field_size - Board.field_size * 0.26 * 2) / 2);
            king = true;
        } else if((type == CheckerType.WHITE && y == 0) || king){
            shape.setFill(Color.valueOf("#00FFFF")); //color white king
            shape.setStroke(Color.BLACK);//kolor zewnetrznych krawedzi
            shape.setStrokeWidth(Board.field_size * 0.03);

            shape.setTranslateX((Board.field_size - Board.field_size * 0.3125 * 2) / 2);
            shape.setTranslateY((Board.field_size - Board.field_size * 0.26 * 2) / 2);
            king = true;
        } else {
        */
        shape.setFill(type == CheckerType.BLACK ? Color.valueOf("#2F4F4F") : Color.valueOf("#FFFAFA"));
        shape.setStroke(Color.BLACK);//color external edges
        shape.setStrokeWidth(Board.field_size * 0.03);

        shape.setTranslateX((Board.field_size - Board.field_size * 0.3125 * 2) / 2);
        shape.setTranslateY((Board.field_size - Board.field_size * 0.26 * 2) / 2);
        

        Ellipse shadow = new Ellipse(Board.field_size * 0.3125, Board.field_size * 0.26); //add a shadow
        shadow.setFill(Color.BLACK);
        shadow.setStroke(Color.BLACK);//color external edges
        shadow.setStrokeWidth(Board.field_size * 0.03);

        shadow.setTranslateX((Board.field_size - Board.field_size * 0.3125 * 2) / 2);
        shadow.setTranslateY((Board.field_size - Board.field_size * 0.26 * 2) / 2 + Board.field_size * 0.07);

        getChildren().addAll(shadow, shape);
        //get the coordinates of a object after click
        setOnMousePressed(c -> {
            clickX = c.getSceneX(); 
            clickY = c.getSceneY();
        });
        //move thanks to move object by mouse
        setOnMouseDragged(c -> {
            relocate(c.getSceneX() - clickX + oldX, c.getSceneY() - clickY + oldY); 
        });

    }

    public void mv(int x, int y){
        oldX = x * Board.field_size;
        oldY = y * Board.field_size;
        relocate(oldX, oldY);
    }

    public void blockMv(){
        relocate(oldX, oldY);
    }

    public double getOldX(){
        return oldX;
    }
    public double getOldY(){
        return oldY;
    }

}
