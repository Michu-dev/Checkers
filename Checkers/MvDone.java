public class MvDone {
    private MvType type;
    private Checker checker;

    public MvDone(MvType t1){
        type = t1;
        checker = null;
    }

    public MvDone(MvType t1, Checker c1){
        type = t1;
        checker = c1;
    }
    public Checker getChecker(){
        return checker;
    }

    public MvType getType(){
        return type;
    }

}
