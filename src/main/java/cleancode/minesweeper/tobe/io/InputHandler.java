package cleancode.minesweeper.tobe.io;

import cleancode.minesweeper.tobe.UserAction.UserAction;
import cleancode.minesweeper.tobe.position.CellPosition;

public interface InputHandler {

    CellPosition getCellPositionFromUser();

    UserAction getUserActionFromUser();
}
