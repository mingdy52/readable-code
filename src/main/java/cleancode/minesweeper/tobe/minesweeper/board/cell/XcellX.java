package cleancode.minesweeper.tobe.minesweeper.board.cell;

// 사용 안함
public abstract class XcellX {
    public abstract boolean isLandMine();
    public abstract boolean hasLandMineCount();
    public abstract String getSign();

    protected static final String FLAG_SIGN = "⚑";
    protected static final String UNCHECKED_SIGN = "□";


    protected boolean isFlaged;
    protected boolean isOpened;

    public void flag() {
        this.isFlaged = true;
    }

    public void open() {
        this.isOpened = true;
    }

    public boolean isChecked() {
        return isFlaged || isOpened;
    }

    public boolean isOpened() {
        return isOpened;
    }

}