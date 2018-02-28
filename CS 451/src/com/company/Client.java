import java.util.*;
import java.io.*;
import java.net.*;

public class Client extends Host {

    public SocketManager socketManager;
    public GameManager gameManager;
    public Socket client;

    @Override
    public void initSession() {
        socketManager = new SocketManager();
        client = socketManager.initSocket();

        String ip = ""; // get ip from user?

        if(!socketManager.isConnected()) {
            if(!socketManager.connect(ip))
                System.out.println("Connection failed");
        }
    }

    @Override
    public void quit() {
        try {
            System.out.println("You choose to quit the game. Back to main menu.")
            client.close();
        } catch(IOException e) {
            System.out.println(e.getStackTrace());
        }
    }

    @Override
    public void playGame() {
        gameManager = new GameManager();
        initSession();

        try {
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
        } catch(IOException e) {

        }

        while(!gameOver()) {
            if(!socketManager.waitForMessage()) {
                System.out.println("Can't receive message");
            }
            else {
                try {
                    if( (board = (GameBoard) in.readObject()) != null ) {

                        //update board in game manager here
                    }
                } catch(ClassNotFoundException e) {

                } catch(IOException e) {

                }

            }

            if(!socketManager.sendMessage())
                System.out.println("Can't send message");
            else {
                out.writeObject(board);
            }
        }
    }
}
