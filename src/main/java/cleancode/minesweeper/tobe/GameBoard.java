package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.cell.*;
import cleancode.minesweeper.tobe.cell.Cell;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.position.CellPosition;
import cleancode.minesweeper.tobe.position.CellPositions;
import cleancode.minesweeper.tobe.position.RelativePosition;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class GameBoard {

    private final Cell[][] board;
    private final int landMineCount;

    public GameBoard(GameLevel gameLevel) {
        int rowSize = gameLevel.getRowSize();
        int colSize = gameLevel.getColSize();
        board = new Cell[rowSize][colSize];

        landMineCount = gameLevel.getLandMineCount();
    }

    public void flagAt(CellPosition cellInput) {
        findCell(cellInput).flag();
    }

    public void openAt(CellPosition cellPosition) {
        findCell(cellPosition).open();
    }

    public void openSurroundedCells(CellPosition cellPosition) {

        if (isOpenedCell(cellPosition)) {
            return;
        }
        if (isLandMineCellAt(cellPosition)) {
            return;
        }

        openAt(cellPosition);

        if (doesCellHaveLandMineCount(cellPosition)) {
            return;
        }

        calculateSurroundedPositions(cellPosition, getRowSize(), getColSize())
                .forEach(this::openSurroundedCells);

        /*
        for (RelativePosition relativePosition : RelativePosition.SURROUNDED_POSITIONS) {
            if(cellPosition.canCalculatePositionBy(relativePosition)) {

                CellPosition nextCellPosition = cellPosition.calculatePositionBy(relativePosition);
                openSurroundedCells(nextCellPosition);
            }
        }
        */

    }

    public boolean isLandMineCellAt(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.isLandMine();
    }

    public boolean isAllCellChecked() {
        Cells cells = Cells.from(board);
        return cells.isAllCellChecked();
    }


    public boolean isInvalidCellPosition(CellPosition cellPosition) {
        int rowSize = getRowSize();
        int colSize = getColSize();
        return cellPosition.isRowIndexMoreThanOrEqual(rowSize)
                || cellPosition.isColIndexMoreThanOrEqual(colSize);
    }


    public void initializeGame() {
        CellPositions cellPositions = CellPositions.from(board);

        initializeEmptyCells(cellPositions);

        List<CellPosition> landMinePositions = cellPositions.extractRandomPositions(landMineCount);
        initializeLandMineCells(landMinePositions);

        List<CellPosition> numberPositionCandidates = cellPositions.subtrace(landMinePositions);
        initializeNumberCells(numberPositionCandidates);

    }

    private void initializeNumberCells(List<CellPosition> numberPositionCandidates) {
        for (CellPosition candidatePosition : numberPositionCandidates) {
            int count = countNearByLandMines(candidatePosition);
            if(count != 0) {
                updateCellAt(candidatePosition, new NumberCell(count));
            }
        }
    }

    private void initializeLandMineCells(List<CellPosition> landMinePositions) {
        for (CellPosition landMinePosition : landMinePositions) {
            updateCellAt(landMinePosition, new LandMineCell());
        }
    }

    private void initializeEmptyCells(CellPositions cellPositions) {
        List<CellPosition> allPositions = cellPositions.getPositions();
        for (CellPosition position : allPositions) {
            updateCellAt(position, new EmptyCell());
        }
    }

    private void updateCellAt(CellPosition position, Cell cell) {
        board[position.getRowIndex()][position.getColIndex()] = cell;
    }

    public int getRowSize() {
        return board.length;
    }
    public int getColSize() {
        return board[0].length;
    }
    public String getSign(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.getSign();
    }

    private Cell findCell(CellPosition cellPosition) {
        return board[cellPosition.getRowIndex()][cellPosition.getColIndex()];
    }

    private boolean isOpenedCell(CellPosition cellPosition) {
        return findCell(cellPosition).isOpened();
    }

    private boolean doesCellHaveLandMineCount(CellPosition cellPosition) {
        return findCell(cellPosition).hasLandMineCount();
    }

    private int countNearByLandMines(CellPosition cellPosition) {
        int rowSize = getRowSize();
        int colSize = getColSize();

        List<CellPosition> surroundedPositions = calculateSurroundedPositions(cellPosition, rowSize, colSize);

        long count = calculateSurroundedPositions(cellPosition, rowSize, colSize).stream()
            .filter(this::isLandMineCellAt)
            .count();

        return (int) count;
    }

    private List<CellPosition> calculateSurroundedPositions(CellPosition cellPosition, int rowSize, int colSize) {
        return RelativePosition.SURROUNDED_POSITIONS.stream()
                .filter(cellPosition::canCalculatePositionBy)
                .map(cellPosition::calculatePositionBy)
                .filter(position -> position.isRowIndexLessThan(rowSize))
                .filter(position -> position.isColIndexLessThan(colSize))
                .toList();
    }

}
