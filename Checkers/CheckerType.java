

public enum CheckerType {
    BLACK(1), WHITE(-1), AQUA(0), BROWN(0);
    final int mvDirection;
    CheckerType(int mv){
        mvDirection = mv;
    }
}
