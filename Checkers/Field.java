
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Field extends Rectangle {

    private Checker checker;

    public boolean hasChecker(){
        return checker != null;
    }

    public Checker getChecker(){
        return checker;
    }

    public void setChecker(Checker checker){
        this.checker = checker;
    }

    public Field(double x, double y, boolean color){
        setWidth(Board.field_size);
        setHeight(Board.field_size);
        relocate(x * Board.field_size, y * Board.field_size);
        if(color)//dark gold
            setFill(Color.valueOf("#FFFAF0"));
        else //white
            setFill(Color.valueOf("#B8860B"));
        

    }
}
