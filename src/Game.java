import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by omar on 11/29/15.
 */
interface SudokuGameListener{
    void moveInGame(String cmd);
}

public class Game extends JFrame implements ActionListener, SudokuBoardListener{
    private ArrayList<SudokuGameListener> listeners = new ArrayList<SudokuGameListener>();
    Player player;
    private SudokuBoard board;
    JButton newGameButton;
    JComboBox<Difficulty> levelComboBox;

    public Game(Player p){
        player = p;
        this.setupGUI();
    }

    public void setupGUI(){
        this.setLayout(new BorderLayout());
        JPanel panel = new JPanel(new BorderLayout());

        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setTitle("Sudoku Multi Player");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        if(player == Player.Host){
            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new FlowLayout());
            controlPanel.add(new Label("Difficulty"));
            controlPanel.add(levelComboBox = new JComboBox<Difficulty>(Difficulty.values()));
            controlPanel.add(newGameButton = new JButton("New Game"));
            newGameButton.addActionListener(this);

            panel.add("North", controlPanel);
        }


        Sudoku sudoku = new Sudoku(Difficulty.Easy);

        board = new SudokuBoard(sudoku, player);
        board.addSudokuBoardListener(this);
        panel.add("Center", board);

        this.getContentPane().add(panel);
        this.pack();
        this.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        this.setVisible(true);
    }

    public void addSudokuGameListener(SudokuGameListener listener){
        listeners.add(listener);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource().equals(newGameButton)){
            int boardNo = board.newGame((Difficulty)levelComboBox.getSelectedItem());
            if(player == Player.Host){
                for(SudokuGameListener listener : listeners){
                    listener.moveInGame("new:"+((Difficulty) levelComboBox.getSelectedItem()).ordinal()+","+boardNo);
                }
            }
        }
    }

    public void networkError(String error){
        JOptionPane.showMessageDialog(null, error, "Network Error", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }

    @Override
    public void moveInBoard(String cmd){
        for(SudokuGameListener listener : listeners){
            listener.moveInGame(cmd);
        }
    }

    public void processCommand(String cmd){
        String[] commands = cmd.split(";");
        for(String command : commands){
            String[] val;
            String[] c = command.split(":");
            switch(c[0]){
                case "new":
                    val = c[1].split(",");
                    board.newGame(Integer.parseInt(val[0]), Integer.parseInt(val[1]));
                    break;
                case "select":
                    val = c[1].split(",");
                    board.changeTileState(Integer.parseInt(val[0]), Integer.parseInt(val[1]), true);
                    break;
                case "deselect":
                    val = c[1].split(",");
                    board.changeTileState(Integer.parseInt(val[0]), Integer.parseInt(val[1]), false);
                    break;
                case "type":
                    val = c[1].split(",");
                    board.typeValue(Integer.parseInt(val[0]), Integer.parseInt(val[1]), Integer.parseInt(val[2]));
                    break;
                case "won":
                    JOptionPane.showInputDialog(null, "You won!", "Winner!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    @Override
    public void wonEvent(){
        JOptionPane.showMessageDialog(null, "You won!", "Winner!", JOptionPane.INFORMATION_MESSAGE);
    }
}
