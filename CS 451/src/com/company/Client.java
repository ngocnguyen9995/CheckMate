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
    public void doTurn() {
        int[] pieces;

        board.displayBoard();

        while(true) {
            System.out.print("Choose a piece: ");
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();

            pieces = gameManager.checkInput(input);

            if (pieces.length == 2) {

                int row = pieces[0];
                int col = pieces[1];

                gameManager.isHost = false;

                if (gameManager.validatePiece(row, col)) {
                    System.out.print("Choose a position to move: ");
                    String position = sc.nextLine();

                    pieces = gameManager.checkInput(position);

                    if(pieces.length == 2) {

                        int newRow = pieces[0];
                        int newCol = pieces[1];

                        if (gameManager.validateMove(row, col, newRow, newCol)) {

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

    @Override
    public void playGame() {
        gameManager = new GameManager();
        board = gameManager.getBoard();
        initSession();

        board.displayBoard();
        while(!gameManager.gameOver()) {

            // read board
            board = (GameBoard) socketManager.waitForMessage(client);

            if(board == null) {
                System.out.println("Can't read message");
                break;
            }

            System.out.println("\nHost moved.");
            gameManager.readBoard(board);

            doTurn();
            // send board
            socketManager.sendMessage(client, board);
        }

    }
}
