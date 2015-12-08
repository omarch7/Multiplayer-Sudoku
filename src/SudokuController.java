/**
 * Created by omar on 12/6/15.
 */
enum Player{
    Host,
    Client
}

public class SudokuController implements SudokuGameListener, NetworkControllerListener{
    Game sudokuGame;
    Player player;
    NetworkController networkController;

    public SudokuController(Player p, String cVal){
        this.player = p;
        networkController = new  NetworkController(this.player, cVal);
        networkController.addNetworkControllerListener(this);

        sudokuGame = new Game(player);
        sudokuGame.addSudokuGameListener(this);
    }

    public void startGame(){
        System.out.println(player.name());
        networkController.execute();
    }

    @Override
    public void moveInGame(String cmd){
        System.out.println("Local "+ cmd);
        networkController.os.println(cmd);
        networkController.os.flush();
    }

    @Override
    public void networkError(String error){
        sudokuGame.networkError(error);
    }

    @Override
    public void incomingData(String cmd){
        sudokuGame.processCommand(cmd);
    }
}
