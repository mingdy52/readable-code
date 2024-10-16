package cleancode.minesweeper.tobe.minesweeper.board;

import cleancode.minesweeper.tobe.minesweeper.board.cell.*;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPositions;
import cleancode.minesweeper.tobe.minesweeper.board.position.RelativePosition;
import cleancode.minesweeper.tobe.minesweeper.gamelevel.GameLevel;

import java.util.List;
import java.util.Stack;

public class GameBoard {
    private GameStatus gameStatus;
    private final Cell[][] board;
    private final int landMineCount;

    public GameBoard(GameLevel gameLevel) {
        int rowSize = gameLevel.getRowSize();
        int colSize = gameLevel.getColSize();
        board = new Cell[rowSize][colSize];

        landMineCount = gameLevel.getLandMineCount();
        initializeGameStatus();
    }

    // 상태 변경
    public void initializeGame() {
        initializeGameStatus();
        CellPositions cellPositions = CellPositions.from(board);

        initializeEmptyCells(cellPositions);

        List<CellPosition> landMinePositions = cellPositions.extractRandomPositions(landMineCount);
        initializeLandMineCells(landMinePositions);

        List<CellPosition> numberPositionCandidates = cellPositions.subtrace(landMinePositions);
        initializeNumberCells(numberPositionCandidates);

    }

    public void openAt(CellPosition cellPosition) {
        if (isLandMineCellAt(cellPosition)) {
            openOneCell(cellPosition);
            changeGameStatusToLose();
            return;
        }

        // openSurroundedCells(cellPosition);
        openSurroundedCells2(cellPosition);
        checkIfGameIsOver();
    }

    public void flagAt(CellPosition cellInput) {
        findCell(cellInput).flag();

        checkIfGameIsOver();
    }

    // 판별
    public boolean isInvalidCellPosition(CellPosition cellPosition) {
        int rowSize = getRowSize();
        int colSize = getColSize();
        return cellPosition.isRowIndexMoreThanOrEqual(rowSize)
                || cellPosition.isColIndexMoreThanOrEqual(colSize);
    }

    public boolean isInProgeress() {
        return gameStatus == GameStatus.IN_PROGRESS;
    }

    public boolean isWinStatus() {
        return gameStatus == GameStatus.WIN;
    }

    public boolean isLoseStatus() {
        return gameStatus == GameStatus.LOSE;
    }

    // 조회
    public CellSnapshot getSnapshot(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.getCellSnapshot();
    }
    public int getRowSize() {
        return board.length;
    }
    public int getColSize() {
        return board[0].length;
    }


    private void initializeGameStatus() {
        gameStatus = GameStatus.IN_PROGRESS;
    }
    private void initializeEmptyCells(CellPositions cellPositions) {
        List<CellPosition> allPositions = cellPositions.getPositions();
        for (CellPosition position : allPositions) {
            updateCellAt(position, new EmptyCell());
        }
    }
    private void initializeLandMineCells(List<CellPosition> landMinePositions) {
        for (CellPosition landMinePosition : landMinePositions) {
            updateCellAt(landMinePosition, new LandMineCell());
        }
    }
    private void initializeNumberCells(List<CellPosition> numberPositionCandidates) {
        for (CellPosition candidatePosition : numberPositionCandidates) {
            int count = countNearByLandMines(candidatePosition);
            if(count != 0) {
                updateCellAt(candidatePosition, new NumberCell(count));
            }
        }
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

    private void updateCellAt(CellPosition position, Cell cell) {
        board[position.getRowIndex()][position.getColIndex()] = cell;
    }

    private void openSurroundedCells(CellPosition cellPosition) {

        if (isOpenedCell(cellPosition)) {
            return;
        }
        if (isLandMineCellAt(cellPosition)) {
            return;
        }

        openOneCell(cellPosition);

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
    private void openSurroundedCells2(CellPosition cellPosition) {
        Stack<CellPosition> stack = new Stack<>();
        stack.push(cellPosition);
        
        while(!stack.isEmpty()) {
            openAandPushCellAt(stack);
        }

    }

    private void openAandPushCellAt(Stack<CellPosition> stack) {
        CellPosition currentCellPosition = stack.pop();

        if (isOpenedCell(currentCellPosition)) {
            return;
        }
        if (isLandMineCellAt(currentCellPosition)) {
            return;
        }

        openOneCell(currentCellPosition);

        if (doesCellHaveLandMineCount(currentCellPosition)) {
            return;
        }

        List<CellPosition> surroundedPositions = calculateSurroundedPositions(currentCellPosition, getRowSize(), getColSize());
        for (CellPosition surroundedPosition : surroundedPositions) {
            stack.push(surroundedPosition);
        }
    }

    private void openOneCell(CellPosition cellPosition) {
        findCell(cellPosition).open();
    }

    private boolean isOpenedCell(CellPosition cellPosition) {
        return findCell(cellPosition).isOpened();
    }
    private boolean isLandMineCellAt(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.isLandMine();
    }
    private boolean doesCellHaveLandMineCount(CellPosition cellPosition) {
        return findCell(cellPosition).hasLandMineCount();
    }

    private void checkIfGameIsOver() {
        if (isAllCellChecked()) {
            changeGameStatusToWin();
        }
    }
    private boolean isAllCellChecked() {
        Cells cells = Cells.from(board);
        return cells.isAllCellChecked();
    }

    private void changeGameStatusToWin() {
        gameStatus = GameStatus.WIN;
    }
    private void changeGameStatusToLose() {
        gameStatus = GameStatus.LOSE;
    }

    private Cell findCell(CellPosition cellPosition) {
        return board[cellPosition.getRowIndex()][cellPosition.getColIndex()];
    }

}
