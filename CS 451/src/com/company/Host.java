import java.util.*;
import java.net.*;
import java.io.*;

public class Host {

    private SocketManager socketManager;
    private GameManager gameManager;
    private ServerSocket serverSocket; //create connection
    private Socket server; //actually connect to it
    private GameBoard board;

    public void initSession() {
        socketManager = new SocketManager();
        gameManager = new GameManager();
        serverSocket = socketManager.initServer(); //initServer() must return a ServerSocket
        server = socketManager.listen();
    }

    public void quit() {
        try {
            System.out.println("You choose to quit the game. Back to main menu.")
            server.close();
        } catch(IOException e) {
            System.out.println(e.getStackTrace());
        }
    }

    public void doTurn() {
        int[] pieces;

        while(true) {
            System.out.print("Choose a piece: ");
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();

            pieces = gameManager.checkInput(input);

            if (pieces.length == 2) {

                int row = pieces[0];
                int col = pieces[1];

                gameManager.isHost = true;

                if (gameManager.validatePiece(row, col)) {
                    System.out.print("Choose a position to move: ");
                    String position = sc.nextLine();

                    pieces = gameManager.checkInput(position);

                    if(pieces.length == 2) {

                        int newRow = pieces[0];
                        int newCol = pieces[1];

                        if (gameManager.validateMove(newRow, newCol)) {

                            board = gameManager.updateBoard(row, col, newRow, newCol);

                            gameManager.isHost = false;
                            break;
                        }
                        else {
                            System.out.println("Invalid position. Please choose again");
                        }
                    }
                    else {
                        System.out.println("Invalid format of input. Please choose again");
                    }
                }
                else{
                    System.out.println("Invalid piece. Please choose again");
                }
            }
            else {
                System.out.println("Invalid format of input. Please choose again");
            }
        }
    }

    public void playGame() {

        gameManager = new GameManager();
        initSession();

        try {
            ObjectInputStream in = new ObjectInputStream(server.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());


            doTurn();
            while(!gameOver()) {

                if(!socketManager.sendMessage()) {
                    System.out.println("Can't send message");
                    break;
                }
                else {
                    out.writeUTF(GameManager.displayBoard());
                }

                if(!socketManager.waitForMessage()) {
                    System.out.println("Can't receive message");
                    break;
                }
                else {
                    try {
                        if( (board = (GameBoard) in.readUTF()) != null ) {

                            //update board in game manager here
                        }
                    } catch(ClassNotFoundException e) {
                        System.out.println(e.getStackTrace());
                    } catch(IOException e) {
                        System.out.println(e.getStackTrace());
                    }

                }
            }
        } catch(IOException e) {
            System.out.println(e.getStackTrace());
        }
    }
}
