import java.util.*;
import java.net.*;
import java.io.*;

package com.company;

public class Host {

    private SocketManager socketManager;
    private GameManager gameManager;
    private ServerSocket serverSocket;
    private Socket server;
    private GameBoard board;
    private int row = 1, col = 1, newRow = 1, newCol = 1;

    public void initSession() {
        socketManager = new SocketManager();
        gameManager = new GameManager();

        try {
            serverSocket = socketManager.initServer();
            server = socketManager.listen(serverSocket);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void quit() {
        try {
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


        while(true) {
            // jump
            System.out.println("");
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
                                    System.out.println("");
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
                                System.out.println("");
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

        System.out.println("\n***************** Game start *****************\n");
        System.out.println("You are player 1. Choose \'x\' to move.");

        System.out.println("\n**********************************************\n");
        board.displayBoard();

        System.out.println("\nPlayer 1 turn.");
        System.out.println("Choose an option: 1. Make move  2. Quit game");
        Scanner sc = new Scanner(System.in);
        int option;

        try {
            option = Integer.parseInt(sc.nextLine());
            while(true) {
                if (option == 1) {

                    if(gameManager.gameOver() != true) {

                        doTurn();
                        socketManager.sendMessage(server, board);

                        System.out.println("\n******** You moved. Wait for Player 2 ********\n");

                        // read board
                        board = (GameBoard) socketManager.waitForMessage(server);

                        if (board == null) {
                            System.out.println("Player 2 exit.\nQuit to main menu");
                            quit();
                            break;
                        }

                        gameManager.readBoard(board);
                        board = gameManager.getBoard();
                        board.displayBoard();
                        gameManager.printPiecesLeft();

                    }
                    else {
                        System.out.println("Game over");
                        break;
                    }
                    System.out.println("\nPlayer 1 turn.");
                    System.out.println("Choose an option: 1. Make move  2. Quit game");
                    option = Integer.parseInt(sc.nextLine());

                } else if (option == 2) {
                    System.out.println("You choose to quit the game. Back to main menu.");
                    quit();
                    break;
                }
                else {
                    System.out.println("Invalid option.");
                    System.out.println("Choose an option: 1. Make move  2. Quit game");
                    option = Integer.parseInt(sc.nextLine());
                }
            }
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }


}
