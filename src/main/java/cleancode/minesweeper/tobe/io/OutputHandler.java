package cleancode.minesweeper.tobe.io;

import cleancode.minesweeper.tobe.GameBoard;
import cleancode.minesweeper.tobe.GameException;

import java.util.List;
import java.util.stream.IntStream;

public interface OutputHandler {

        public void showGameStartCommnets();

        public void showBoard(GameBoard board);

        public void showGameWinningComment();

        public void showGameLosingComment();

        public void showCommentForSelectingCell();

        public void showCommentForUserAction();

        public void showExceptionMessage(GameException e);
        public void showSimpleMessage(String message);

}
