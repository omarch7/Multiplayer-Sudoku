import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Created by omar on 12/5/15.
 */
public class SudokuTile extends JComponent{
    private static final Color evenBoxColor = new Color(2, 178, 255);
    private static final Color oddBoxColor = new Color(255, 100, 47);
    private static final Color hostColor = new Color(80, 153, 57);
    private static final Color guestColor = new Color(113,54,151);

    private Player player = null;

    private boolean isOwner = false;
    private boolean inEvenBox = false;


    private boolean isSelected = true;
    private int number = 0;
    public int x, y;
    public SudokuTile(){
        this.setBorder(new LineBorder(Color.WHITE, 1));
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public SudokuTile(boolean box){
        inEvenBox = box;
        this.setBorder(new LineBorder(Color.WHITE, 1));
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        float width = getWidth();
        float height = getHeight();
        super.paintComponent(g);

        g2.setColor(inEvenBox ? evenBoxColor : oddBoxColor);
        g2.fillRect(0, 0, (int)width, (int)height);

        if(number>0){
            if(player!=null){
                g2.setColor(player==Player.Host ? hostColor : guestColor);
            }else{
                g2.setColor(Color.WHITE);
            }
            Font font = new Font("Helvetica", Font.TRUETYPE_FONT, 20);
            int fontWidth = g2.getFontMetrics(font).stringWidth(Integer.toString(number));
            int fontHeight = g2.getFontMetrics(font).getHeight();
            g2.setFont(font);
            g2.drawString(Integer.toString(number), (width/2) - (fontWidth / 2), (height/2) + (fontHeight / 2) - 2);
        }
    }

    public void setNumber(int val){
        if(val>=0){
            number = val;
            isOwner = false;
            player = null;
            this.setSelected(false);
            if(val>0){
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            repaint();
        }
    }

    public void typeNumber(int val){
        if(isOwner){
            number = val;
            repaint();
        }
    }

    public void setCoordinates(int cX, int cY){
        x = cX;
        y = cY;
    }

    public boolean isSelected(){
        return isSelected;
    }

    public boolean isEmpty(){
        return number == 0 ? true : false;
    }

    public void setSelected(boolean selected){
        isSelected = selected;
        if(isSelected){
            isOwner = true;
            this.setBorder(new LineBorder(player == Player.Host ? hostColor : guestColor , 2));
        }else{
            if(number==0){
                player = null;
            }
            this.setBorder(new LineBorder(Color.WHITE, 1));
        }
    }

    public boolean isEquals(SudokuTile tile){
        return tile.x == this.x && tile.y == this.y ? true : false;
    }

    public boolean isOwner(){
        return isOwner;
    }

    public void setOwner(boolean owner){
        isOwner = owner;
        if(isOwner){
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }else{
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    public Player getPlayer(){
        return player;
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public int getNumber(){
        return number;
    }
}
