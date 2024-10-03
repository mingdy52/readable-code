package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.gamelevel.Middle;
import cleancode.minesweeper.tobe.io.ConsoleInputHandler;
import cleancode.minesweeper.tobe.io.ConsoleOutputHandler;
import cleancode.minesweeper.tobe.io.InputHandler;
import cleancode.minesweeper.tobe.io.OutputHandler;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class GameApplication {
    public static void main(String[] args) {
        GameLevel gameLevel = new Middle();
        InputHandler inputHandler = new ConsoleInputHandler();
        OutputHandler outputHandler = new ConsoleOutputHandler();

        Minesweeper minesweeper = new Minesweeper(gameLevel, inputHandler, outputHandler);
        minesweeper.initialize();
        minesweeper.run();
    }

    /*
        DIP: Dependency Inversion Principle
            고수준 모듈과 저수준 모듈이 직접적으로 의존하는 것이 아니라 추상화에 의해서 의존 해야 한다.

        DI: Dependency Injection
            의존성 주입
            - 필요한 의존성으로 직접 생성하는 게 아니라 외부에서 주입받겠다.

        IoC: Inversion of Control
            제어의 역전
            - 프로그램의 제어의 주도권을 개발자가 아닌 프레임워크가 담당하도록 한다.
                제어의 순방향: 프로그램의 흐름을 개발자가 주도
                제어의 역방향: 공장 같은 프레임워크가 있고 내 코드가 공장의 일부가 되어 동작한다.

            - IoC 컨테이너가 객체를 생성하고 생명 주기를 관리해준다.
                빈의 생성, 빈들끼리의 의존성 주입...

    */
}
