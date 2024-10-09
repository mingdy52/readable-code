package cleancode.minesweeper.tobe.cell;

import cleancode.minesweeper.tobe.cell.Cell;

import java.util.Arrays;
import java.util.List;

public class Cells {
    private final List<Cell> cells;

    private Cells(List<Cell> cells) {
        this.cells = cells;
    }

    public static Cells of(List<Cell> cells) {
        return new Cells(cells);
    }

    public static Cells from(Cell[][] cells) {
        List<Cell> cellList = Arrays.stream(cells)
                .flatMap(Arrays::stream)
                .toList();
        return of(cellList);
    }

    public boolean isAllCellChecked() {
        return cells.stream()
                .allMatch(Cell::isChecked);
    }
}
