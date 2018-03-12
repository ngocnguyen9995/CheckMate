import java.util.*;
import java.net.*;
import java.io.*;

public class Host {

    private SocketManager socketManager;
    private GameManager gameManager;
    private ServerSocket serverSocket; //create connection
    private Socket server; //actually connect to it
    private GameBoard board;
    private int row = 1, col = 1, newRow = 1, newCol = 1;

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
        String[] canJumpPieces;
        int row, col, newRow, newCol;
        Scanner sc = new Scanner(System.in);
        String input, position;
        gameManager.isHost = true;

        board.displayBoard();

        while(true) {
            // jump
            if(gameManager.searchJump()) { //searchJump return true if there is jump

                System.out.print("Choose a piece to jump: ");
                input = sc.nextLine();

                pieces = gameManager.checkInput(input);

                if (pieces.length == 2) {
                    row = pieces[0];
                    col = pieces[1];

                    if(gameManager.canJump(row,col)) {
                        while(gameManager.canJump(row,col)) {
                            System.out.print("Choose a position to jump to: ");
                            position = sc.nextLine();

                            pieces = gameManager.checkInput(position);

                            if (pieces.length == 2) {
                                newRow = pieces[0];
                                newCol = pieces[1];

                                if (gameManager.jump(row, col, newRow, newCol)) {

                                    board = gameManager.getBoard();
                                    gameManager.printPiecesLeft();
                                    row = newRow;
                                    col = newCol;

                                } else {
                                    System.out.println("Invalid jump position. Please choose again");
                                }
                            } else {
                                System.out.println("Invalid format of input. Please choose again");
                            }
                        }
                        gameManager.isHost = false;
                        break;
                    }
                    else {
                        System.out.println("Invalid piece. Please choose again");
                    }
                }
                else {
                    System.out.println("Invalid format of input. Please choose again");
                }
            }
            // normal move
            else {
                System.out.print("Choose a piece to move: ");
                input = sc.nextLine();

                pieces = gameManager.checkInput(input);

                if (pieces.length == 2) {
                    row = pieces[0];
                    col = pieces[1];

                    if(gameManager.validatePiece(row, col)) {

                        System.out.print("Choose a position to move into: ");
                        position = sc.nextLine();

                        pieces = gameManager.checkInput(position);

                        if (pieces.length == 2) {
                            newRow = pieces[0];
                            newCol = pieces[1];

                            if (gameManager.validateMove(row, col, newRow, newCol)) {

                                gameManager.updateBoard(row, col, newRow, newCol);
                                board = gameManager.getBoard();
                                gameManager.printPiecesLeft();
                                gameManager.isHost = false;
                                break;
                            }
                            else {
                                System.out.println("Invalid move. Please choose again");
                            }
                        }
                        else {
                            System.out.println("Invalid format of input. Please choose again");
                        }
                    }
                    else {
                        System.out.println("Invalid piece. Please choose again");
                    }
                }
                else {
                    System.out.println("Invalid format of input. Please choose again");
                }
            }
        }
    }


    public void playGame() {

        gameManager = new GameManager();
        board = gameManager.getBoard();
        initSession();


        doTurn();
        socketManager.sendMessage(server, board);

        while(gameManager.gameOver() != true) {

            // read board
            board = (GameBoard) socketManager.waitForMessage(server);

            if(board == null) {
                System.out.println("Can't read message");
                break;
            }

            System.out.println("\nClient moved.\n");

            gameManager.readBoard(board);
            board = gameManager.getBoard();
            gameManager.printPiecesLeft();

            if(gameManager.gameOver() == true) {
                System.out.println("Game over");
                break;
            }
            else {
                doTurn();
                socketManager.sendMessage(server, board);
            }
        }

    }
}
