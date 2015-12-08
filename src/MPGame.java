import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by omar on 12/6/15.
 */
public class MPGame extends JFrame implements ActionListener, SudokuConstants{
    JButton hostGameButton;
    JTextField hostPortField;
    JButton joinGameButton;
    JTextField joinAddressField;
    JDialog dialog;
    SudokuController controller;

    public static void main(String args[]){
        MPGame mpGame = new MPGame();
        mpGame.dialog = new JDialog();
        mpGame.dialog.pack();
        JOptionPane.showOptionDialog(mpGame.dialog, mpGame.loginPanel(), "Multi-player Sudoku - Login", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{}, null);
    }

    private JPanel loginPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));
        panel.add(new JLabel("Host Game", SwingConstants.CENTER));
        panel.add(new JLabel("Join Game", SwingConstants.CENTER));
        panel.add(new JLabel("Host Port", SwingConstants.CENTER));
        panel.add(new JLabel("Game Address", SwingConstants.CENTER));

        panel.add(hostPortField = new JTextField(Integer.toString(DEFAULT_PORT), SwingConstants.CENTER));
        panel.add(joinAddressField = new JTextField(DEFAULT_IP + ":" + DEFAULT_PORT, SwingConstants.CENTER));

        panel.add(hostGameButton = new JButton("Host Game"));
        hostGameButton.addActionListener(this);

        panel.add(joinGameButton = new JButton("Join Game"));
        joinGameButton.addActionListener(this);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        this.dialog.dispose();
        if(e.getSource().equals(hostGameButton)){
            controller = new SudokuController(Player.Host, hostPortField.getText().toString());
        }else{
            controller = new SudokuController(Player.Client, joinAddressField.getText().toString());
        }
        controller.startGame();
    }
}