import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar on 12/6/15.
 */
interface NetworkControllerListener{
    void networkError(String error);
    void incomingData(String cmd);
}

public class NetworkController extends SwingWorker<String, String>{
    private ArrayList<NetworkControllerListener> listeners = new ArrayList<NetworkControllerListener>();

    Player player;

    String address;
    ServerSocket server = null;
    Socket mySocket = null;
    BufferedReader is;
    PrintWriter os;

    NetworkController(Player p, String cVal){
        player = p;
        address = cVal;
    }

    public void addNetworkControllerListener(NetworkControllerListener listener){
        listeners.add(listener);
    }

    @Override
    protected String doInBackground() throws Exception{
        while(true){
            if(player == Player.Host){
                try{
                    try{
                        server = new ServerSocket(Integer.parseInt(address));
                    }catch(Exception e){
                        System.out.println("Cannot listen to: " + e);
                        for(NetworkControllerListener listener : listeners){
                            listener.networkError(e.getLocalizedMessage());
                        }
                    }
                    try{
                        mySocket = server.accept();
                    }catch(Exception e){
                        System.out.println("Error: "+ e);
                    }
                    is = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
                    os = new PrintWriter(mySocket.getOutputStream());
                }catch(Exception e){
                    System.out.println("Error: in server Client Socket" + e);
                    for(NetworkControllerListener listener : listeners){
                        listener.networkError(e.getLocalizedMessage());
                    }
                }
            }else{
                try{
                    String[] values = address.split(":");
                    mySocket = new Socket(values[0], Integer.parseInt(values[1]));
                    os = new PrintWriter(mySocket.getOutputStream());
                    is = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
                }catch(Exception e){
                    System.out.println("Error: in client socket "+e);
                    for(NetworkControllerListener listener : listeners){
                        listener.networkError(e.getLocalizedMessage());
                    }
                }
            }
            String line;
            while((line = is.readLine()) != null){
                try{
                    publish(line);
                    System.out.println(line);
                }catch(Exception e){
                    System.out.println("Error: in receive remote information: "+e);
                    for(NetworkControllerListener listener : listeners){
                        listener.networkError(e.getLocalizedMessage());
                    }
                }
            }
        }
    }

    @Override
    protected void process(List<String> chunks){
        super.process(chunks);
        for(NetworkControllerListener listener : listeners){
            listener.incomingData(chunks.get(0));
        }
    }

    @Override
    protected void done(){
        super.done();
    }
}
