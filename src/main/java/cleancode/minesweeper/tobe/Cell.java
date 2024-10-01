package cleancode.minesweeper.tobe;

public class Cell {
    private static final String FLAG_SIGN = "⚑";
    private static final String LAND_MINE_SIGN = "☼";
    private static final String UNCHECKED_SIGN = "□";
    private static final String EMPTY_SIGN = "■";

    private int nearbyLandMineCount;
    private boolean isLandMine;
    private boolean isFlaged;
    private boolean isOpened;

    // Cell이 가진 속성: 근처 지뢰 숫자, 지뢰 여부
    // Cell이 상태: 깃발 유무, 열렸다/닫혔다, 사용자가 확인함
    private Cell(int nearbyLandMineCount, boolean isLandMine, boolean isFlaged, boolean isOpened) {
        this.nearbyLandMineCount = nearbyLandMineCount;
        this.isLandMine = isLandMine;
        this.isFlaged = isFlaged;
        this.isOpened = isOpened;
    }

    public static Cell of(int nearbyLandMineCount, boolean isLandMine, boolean isFlaged, boolean isOpened) {
        return new Cell(nearbyLandMineCount, isLandMine, isFlaged, isOpened);
    }

    public static Cell create() {
        return of(0, false, false, false);
    }

    public void turnOnLandMine() {
        this.isLandMine = true;
    }

    public void flag() {
        this.isFlaged = true;
    }

    public void open() {
        this.isOpened = true;
    }

    public boolean isLandMine() {
        return isLandMine;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public boolean isChecked() {
        return isFlaged || isOpened;
    }

    public boolean hasLandMineCount() {
        return this.nearbyLandMineCount != 0;
    }

    public void updateNearByLandMineCount(int count) {
        this.nearbyLandMineCount = count;
    }

    public String getSign() {
        if(isOpened) {
            if(isLandMine) return LAND_MINE_SIGN;

            if(hasLandMineCount()) return String.valueOf(nearbyLandMineCount);

            return EMPTY_SIGN;
        }

        if(isFlaged) {
            return FLAG_SIGN;
        }

        return UNCHECKED_SIGN;
    }
}