package life;

public class UniverseNextStageGen {

    private boolean[][] nxtStage;
    private GridCell cell;
    private Universe universe;

    public UniverseNextStageGen(Universe universe) {
        int dim = universe.getDimension();
        cell = new GridCell(dim);
        nxtStage = new boolean[dim][dim];
        this.universe = universe;
/*
        cell.setStartPos();
        do {
            Coord curPos = cell.getCurPos();
            nxtStage[curPos.getY()][curPos.getX()] = universe.getCellState(curPos);
        }while(cell.moveNext());

 */
    }

    public void calcNextStage() {
        cell.setStartPos();
        do {
            int val = calcNeighboors();
            Coord curPos = cell.getCurPos();

            if(val == 2 || val == 3) {
                if (universe.getCellState(curPos)) {
                    nxtStage[curPos.getY()][curPos.getX()] = true;
                } else {
                    if (val == 3) {
                        nxtStage[curPos.getY()][curPos.getX()] = true;
                    }
                }
            } else {
                nxtStage[curPos.getY()][curPos.getX()] = false;
            }
        } while (cell.moveNext());
    }

    public void setNextStage() {
        cell.setStartPos();
        int liveCell = 0;
        do {
            Coord curPos = cell.getCurPos();
            boolean cellState = getNextCellState(curPos);
            if(cellState)
                liveCell++;
            universe.setCellState(curPos, cellState);
        } while (cell.moveNext());
        universe.setNextStageParam(liveCell);
    }

    private boolean getNextCellState(Coord coord) {
        return nxtStage[coord.getY()][coord.getX()];
    }

    private int calcNeighboors() {
        int res = 0;
        Coord neibCoord = cell.getNeighbour_E();
        res += (universe.getCellState(neibCoord) ? 1 : 0);
        neibCoord = cell.getNeighbour_SE();
        res += (universe.getCellState(neibCoord) ? 1 : 0);
        neibCoord = cell.getNeighbour_S();
        res += (universe.getCellState(neibCoord) ? 1 : 0);
        neibCoord = cell.getNeighbour_SW();
        res += (universe.getCellState(neibCoord) ? 1 : 0);
        neibCoord = cell.getNeighbour_W();
        res += (universe.getCellState(neibCoord) ? 1 : 0);
        neibCoord = cell.getNeighbour_NW();
        res += (universe.getCellState(neibCoord) ? 1 : 0);
        neibCoord = cell.getNeighbour_N();
        res += (universe.getCellState(neibCoord) ? 1 : 0);
        neibCoord = cell.getNeighbour_NE();
        res += (universe.getCellState(neibCoord) ? 1 : 0);
        return res;
    }
}
