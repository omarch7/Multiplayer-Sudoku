import java.util.Random;

/**
 * Created by omar on 11/29/15.
 */
enum Difficulty{
    Easy,
    Medium,
    Hard,
    Evil
}

public class Sudoku implements Boards{
    private int mBoard[][];
    private int mBoardSize;
    private int mBoxSize;
    private boolean mRowSubset[][];
    private boolean mColSubset[][];
    private boolean mBoxSubset[][];

    /**
     * Sudoku board game!
     *
     * @author Omar Contreras
     * @param board Initialize with an existing board
     * @version 1.0
     */
    public Sudoku(int[][] board){
        mBoard = board;
        mBoardSize = mBoard.length;
        mBoxSize = (int)Math.sqrt(mBoardSize);
        initSubSets();
    }

    /**
     * Sudoku board game!
     *
     * @author Omar Contreras
     * @param level Specify the Difficulty, Easy, Medium or Hard
     * @version 1.0
     */
    public Sudoku(Difficulty level){
        mBoard = new int[9][9];
        mBoardSize = mBoard.length;
        mBoxSize = (int)Math.sqrt(mBoardSize);
        generateBoard(level);
        initSubSets();
    }

    /**
     * Get the 2D Array with the Sudoku Board
     *
     * @return int[][] Sudoku Board
     */
    public int[][] getBoard(){
        return mBoard;
    }

    public int generateBoard(Difficulty level){
        Random rnd = new Random();
        int boardNo = rnd.nextInt(2);
        mBoard = BOARDS[level.ordinal()][boardNo];
        initSubSets();
        return boardNo;
    }

    public void generateBoard(int level, int boardNo){
        mBoard = BOARDS[level][boardNo];
        initSubSets();
    }

    public void initSubSets() {
        mRowSubset = new boolean[mBoardSize][mBoardSize];
        mColSubset = new boolean[mBoardSize][mBoardSize];
        mBoxSubset = new boolean[mBoardSize][mBoardSize];
        for(int i = 0; i < mBoard.length; i++){
            for(int j = 0; j < mBoard.length; j++){
                int value = mBoard[i][j];
                if(value != 0){
                    setSubSetValue(i, j, value, true);
                }
            }
        }
    }

    private void setSubSetValue(int i, int j, int value, boolean present){
        mRowSubset[i][value - 1] = present;
        mColSubset[j][value - 1] = present;
        mBoxSubset[computeBoxNo(i, j)][value - 1] = present;
    }

    public int computeBoxNo(int i, int j){
        int boxRow = i / mBoxSize;
        int boxCol = j / mBoxSize;
        return boxRow * mBoxSize + boxCol;
    }

    public boolean solve(){
        return solve(0, 0);
    }

    public boolean solve(int i, int j){
        if(i  == mBoardSize){
            i = 0;
            if(++j == mBoardSize){
                return true;
            }
        }
        if(mBoard[i][j] != 0){
            return solve(i + 1, j);
        }
        for(int value = 1; value <= mBoardSize; value++){
            if(isValid(i, j, value)){
                mBoard[i][j] = value;
                setSubSetValue(i, j, value, true);
                if(solve(i + 1, j)){
                    return true;
                }
                setSubSetValue(i, j, value, false);
            }
        }
        mBoard[i][j] = 0;
        return false;
    }

    private boolean isValid(int i, int j, int val){
        val--;
        boolean isPresent = mRowSubset[i][val] || mColSubset[j][val] || mBoxSubset[computeBoxNo(i, j)][val];
        return !isPresent;
    }

    public boolean addValue(int i, int j, int val){
        if(isValid(i, j, val)){
            mBoard[i][j] = val;
            initSubSets();
            return true;
        }else{
            return false;
        }
    }

    /**
     * Print the Sudoku board in the Console
     */
    public void print(){
        for(int i = 0; i <mBoardSize; i++){
            if(i % mBoxSize == 0){
                System.out.println(" -----------------------");
            }
            for(int j = 0; j < mBoardSize; j++){
                if(j % mBoxSize == 0){
                    System.out.print("| ");
                }
                System.out.print(mBoard[i][j] != 0 ? Integer.valueOf(mBoard[i][j]) : " ");
                System.out.print(" ");
            }
            System.out.println("|");
        }
        System.out.println(" -----------------------");
    }
}