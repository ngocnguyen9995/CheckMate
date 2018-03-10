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

        try {
            serverSocket = socketManager.initServer(7896);
            server = socketManager.listen(serverSocket);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void quit() {
        try {
            System.out.println("You choose to quit the game. Back to main menu.");
            server.close();
        } catch(IOException e) {
            System.out.println(e.getStackTrace());
        }
    }

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
                int newRow, newCol;

                gameManager.isHost = true;

                if(gameManager.canJump(row, col)) {
                    while (true) {
                        System.out.print("Choose a new position to jump: ");
                        input = sc.nextLine();
                        pieces = gameManager.checkInput(input);

                        if (pieces.length == 2) {
                            newRow = pieces[0];
                            newCol = pieces[1];

                            if (gameManager.validateMove(row, col, newRow, newCol)) {

                                board = gameManager.jump(row, col, newRow, newCol);
                            } else {
                                System.out.println("Invalid position. Please choose again");
                            }
                        } else {
                            System.out.println("Invalid format of input. Please choose again");
                        }
                    }
                }

                else if (gameManager.validatePiece(row, col)) {
                    System.out.print("Choose a position to move into: ");
                    String position = sc.nextLine();

                    pieces = gameManager.checkInput(position);

                    if(pieces.length == 2) {

                        newRow = pieces[0];
                        newCol = pieces[1];

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

    public void playGame() {

        gameManager = new GameManager();
        board = gameManager.getBoard();
        initSession();

        doTurn();
        while(!gameManager.gameOver()) {

            // send board
            socketManager.sendMessage(server, board);

            // read board
            board = (GameBoard) socketManager.waitForMessage(server);

            if(board == null) {
                System.out.println("Can't read message");
                break;
            }

            gameManager.readBoard(board);
            doTurn();
        }

    }
}
