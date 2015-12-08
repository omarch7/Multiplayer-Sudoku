import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Created by omar on 12/5/15.
 */
interface SudokuBoardListener{
    void moveInBoard(String cmd);
    void wonEvent();
}

public class SudokuBoard extends JComponent implements MouseListener, KeyListener{
    private ArrayList<SudokuBoardListener> listeners = new ArrayList<SudokuBoardListener>();
    Player player;
    Sudoku sudokuLogic;
    SudokuTile selectedTile = null;
    ArrayList<SudokuTile> tiles = new ArrayList<>();

    public SudokuBoard(Sudoku sudoku, Player p){
        player = p;
        sudokuLogic = sudoku;
        this.setPreferredSize(new Dimension(400, 400));
        this.setBackground(Color.WHITE);
        this.setLayout(new GridLayout(9, 9));
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                SudokuTile tile = new SudokuTile(sudoku.computeBoxNo(i, j) % 2 == 0 ? true : false);
                tile.setNumber(sudokuLogic.getBoard()[i][j]);
                tile.setCoordinates(i, j);
                tile.addMouseListener(this);
                tile.addKeyListener(this);
                this.add(tile);
                tiles.add(tile);
            }
        }
    }

    public int newGame(Difficulty level){
        int boardNo = sudokuLogic.generateBoard(level);
        for(SudokuTile tile : tiles){
            tile.setNumber(sudokuLogic.getBoard()[tile.x][tile.y]);
        }
        selectedTile = null;
        return boardNo;
    }

    public void newGame(int level, int boardNo){
        sudokuLogic.generateBoard(level, boardNo);
        for(SudokuTile tile : tiles){
            tile.setNumber(sudokuLogic.getBoard()[tile.x][tile.y]);
        }
        selectedTile = null;
    }

    public void changeTileState(int x, int y, boolean selected){
        SudokuTile tile = tiles.stream().filter(t-> t.x == x && t.y == y).findFirst().get();
        tile.setPlayer(player==Player.Host ? Player.Client : Player.Host);
        tile.setSelected(selected);
        if(!selected){
            tile.setOwner(false);
        }
    }

    public void typeValue(int x, int y, int value){
        SudokuTile tile = tiles.stream().filter(t-> t.x == x && t.y == y).findFirst().get();
        tile.setPlayer(player==Player.Host ? Player.Client : Player.Host);
        tile.typeNumber(value);
    }

    public void addSudokuBoardListener(SudokuBoardListener listener){
        listeners.add(listener);
    }

    @Override
    public void mouseClicked(MouseEvent e){
        SudokuTile tile = (SudokuTile)e.getSource();
        if(tile.isEmpty() || tile.getPlayer() == player){
            if(selectedTile==null){
                tile.setPlayer(player);
                tile.setSelected(true);
                selectedTile = tile;
                selectedTile.requestFocus();
                for(SudokuBoardListener listener : listeners){
                    listener.moveInBoard("select:"+tile.x+","+tile.y);
                }
            }else{
                if(!selectedTile.isEquals(tile)){
                    selectedTile.setSelected(false);
                    for(SudokuBoardListener listener : listeners){
                        listener.moveInBoard("select:"+tile.x+","+tile.y+";deselect:"+selectedTile.x+","+selectedTile.y);
                    }
                    tile.setPlayer(player);
                    tile.setSelected(true);
                    selectedTile = tile;
                    selectedTile.requestFocus();
                }else{
                    tile.setSelected(false);
                    selectedTile = null;
                    for(SudokuBoardListener listener : listeners){
                        listener.moveInBoard("deselect:"+tile.x+","+tile.y);
                    }
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e){

    }

    @Override
    public void mouseReleased(MouseEvent e){

    }

    @Override
    public void mouseEntered(MouseEvent e){

    }

    @Override
    public void mouseExited(MouseEvent e){

    }

    @Override
    public void keyTyped(KeyEvent e){

    }

    @Override
    public void keyPressed(KeyEvent e){

    }

    @Override
    public void keyReleased(KeyEvent e){
        if(selectedTile!=null){
            if(selectedTile.isSelected()){
                if(e.getKeyCode() == 8){
                    selectedTile.setPlayer(null);
                    selectedTile.typeNumber(0);
                    for(SudokuBoardListener listener : listeners){
                        listener.moveInBoard("type:"+selectedTile.x+","+selectedTile.y+",0");
                    }
                }else if(e.getKeyCode()>=49 && e.getKeyCode() <= 57){
                    int val = Character.getNumericValue(e.getKeyChar());
                    if(sudokuLogic.addValue(selectedTile.x, selectedTile.y, val)){
                        selectedTile.setPlayer(player);
                        selectedTile.typeNumber(Character.getNumericValue(e.getKeyChar()));
                        for(SudokuBoardListener listener : listeners){
                            listener.moveInBoard("type:"+selectedTile.x+","+selectedTile.y+","+e.getKeyChar());
                            if(tiles.stream().filter(t->t.getNumber()==0).count()==0){
                                listener.moveInBoard("won:1");
                                listener.wonEvent();
                            }
                        }

                    }
                }
            }
        }
    }
}
