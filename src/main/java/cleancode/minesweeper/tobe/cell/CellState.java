package cleancode.minesweeper.tobe.cell;

public class CellState {

    private boolean isFlaged;
    private boolean isOpened;

    public CellState(boolean isFlaged, boolean isOpened) {
        this.isFlaged = isFlaged;
        this.isOpened = isOpened;
    }

    public static CellState initialize() {
        return new CellState(false, false);
    }

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
    public boolean isFlaged() {
        return isFlaged;
    }
}
