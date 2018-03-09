import java.util.*;
import java.io.*;
import java.net.*;

public class Client extends Host {

    private SocketManager socketManager;
    private GameManager gameManager;
    private Socket client;
    private GameBoard board;

    @Override
    public void initSession() {
        socketManager = new SocketManager();
        client = socketManager.initClient();

        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Type in the host IP Address: ");
            String ip = sc.nextLine();
            System.out.print("Type in the host port: ");
            int port = sc.nextInt();

            if (!socketManager.connect(ip, port, client))
                System.out.println("Connection failed");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void quit() {
        try {
            System.out.println("You choose to quit the game. Back to main menu.");
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
            while(!gameManager.gameOver()) {

                board = (GameBoard) socketManager.waitForMessage(client));

                if(board == null)
                    System.out.println("Can't read message");

                doTurn();

                if(!socketManager.sendMessage(client, board))
                    System.out.println("Can't send message");
            }
        } catch(IOException e) {
            System.out.println(e.getStackTrace());
        }
    }
}
