package cleancode.minesweeper.tobe.minesweeper.io;

import cleancode.minesweeper.tobe.minesweeper.UserAction.UserAction;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition;

public interface InputHandler {

    CellPosition getCellPositionFromUser();

    UserAction getUserActionFromUser();
}
