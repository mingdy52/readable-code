package cleancode.minesweeper.tobe.minesweeper.board.cell;

public class LandMineCell implements Cell {

    private final CellState cellState = CellState.initialize();

    @Override
    public boolean isLandMine() {
        return true;
    }

    @Override
    public boolean hasLandMineCount() {
        return false;
    }

    @Override
    public CellSnapshot getCellSnapshot() {
        if(cellState.isOpened()) {
            return CellSnapshot.ofLandMine();
        }

        if(cellState.isFlaged()) {
            return CellSnapshot.ofFlag();
        }

        return CellSnapshot.ofUnchecked();
    }

    @Override
    public void flag() {
        cellState.flag();
    }

    @Override
    public void open() {
        cellState.open();
    }

    @Override
    public boolean isChecked() {
        return cellState.isFlaged();
    }

    @Override
    public boolean isOpened() {
        return cellState.isOpened();
    }
}